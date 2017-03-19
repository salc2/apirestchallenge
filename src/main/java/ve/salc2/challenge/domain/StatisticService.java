package ve.salc2.challenge.domain;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private static final String LAST_MINUTE_STATISTIC = "last_minute_statistic";
    private TransactionRepository transactionRepository;
    private HazelcastInstance hazelcastInstance;
    private IMap<String, Statistic> lastStatisticMap;


    @Autowired
    public StatisticService(TransactionRepository transactionRepository,
                            HazelcastInstance hazelcastInstance) {
        this.transactionRepository = transactionRepository;
        this.hazelcastInstance = hazelcastInstance;
        this.lastStatisticMap = hazelcastInstance.getMap(LAST_MINUTE_STATISTIC);
    }

    private Statistic calculateStatisticBy(final List<Transaction> transactions) {

        final DoubleSummaryStatistics summary = transactions.stream()
                .collect(Collectors.summarizingDouble(Transaction::getAmount));

        return new Statistic(
                summary.getSum(),
                summary.getAverage(),
                summary.getMax() == Double.NEGATIVE_INFINITY ? 0 : summary.getMax(),
                summary.getMin() == Double.POSITIVE_INFINITY ? 0 : summary.getMin(),
                summary.getCount());
    }

    public Statistic statisticByTimestampRange(final long fromTimestamp,
                                               final long untilTimestamp) {
        final List<Transaction> transactions = transactionRepository
                .findByTimestampBetween(fromTimestamp, untilTimestamp);
        return calculateStatisticBy(transactions);
    }

    @Scheduled(fixedRate = 1000)
    public void calculateLastSixtySec(){
        final long until = System.currentTimeMillis();
        final long from = until - (60 * 1000);
        Statistic statistic = statisticByTimestampRange(from, until);
        lastStatisticMap.put("last",statistic);
    }

    public Optional<Statistic> lastSixtySecStatistic(){
        return Optional.ofNullable(lastStatisticMap.get("last"));
    }

    public void clearStatisticCache(){
        lastStatisticMap.clear();
    }
}