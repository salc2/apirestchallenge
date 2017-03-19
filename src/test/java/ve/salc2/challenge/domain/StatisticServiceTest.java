package ve.salc2.challenge.domain;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticServiceTest {

    private static TestHazelcastInstanceFactory testInstanceFactory = new TestHazelcastInstanceFactory();
    private final long current = System.currentTimeMillis();
    private final long fiveSec = current + 5000;

    @Autowired
    private TransactionRepository repository;
    private StatisticService statisticService;

    @Before
    public void setUp() {
        HazelcastInstance hazelcastInstance = testInstanceFactory.newHazelcastInstance();
        statisticService = new StatisticService(repository, hazelcastInstance);

        repository.deleteAll();
        statisticService.clearStatisticCache();

        AtomicLong amountInc = new AtomicLong();
        Stream<Transaction> fiveTxsIn5secStream =
                LongStream.range(current, fiveSec)
                        .boxed()
                        .filter(t -> t % 1000 == 0)
                        .map(t -> {
                            double amount = amountInc.addAndGet(100) + 0.0;
                            return new Transaction(amount, t);
                        });
        fiveTxsIn5secStream.forEach(repository::save);
    }

    @Test
    public void statisticByTimestampRangeCountedFive() throws Exception {

        final Statistic expectedFiveTxs = new Statistic(1500.0, 300.0, 500.0, 100.0, 5);
        final Statistic statistic = statisticService
                .statisticByTimestampRange(current, fiveSec);

        Assertions.assertThat(statistic)
                .isEqualToComparingFieldByField(expectedFiveTxs);

    }


    @Test
    public void statisticByTimestampRangeCountedFour() throws Exception {

        final long fiveSecLess4 = fiveSec - 4000;

        final Statistic statisticFourTx = statisticService
                .statisticByTimestampRange(fiveSecLess4, fiveSec);

        final Statistic expectedFourTxs = new Statistic(1400.0, 350.0, 500.0, 200.0, 4);

        Assertions.assertThat(statisticFourTx)
                .isEqualToComparingFieldByField(expectedFourTxs);
    }

}
