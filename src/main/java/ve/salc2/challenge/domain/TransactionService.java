package ve.salc2.challenge.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private StatisticService statisticService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              StatisticService statisticService) {
        this.transactionRepository = transactionRepository;
        this.statisticService = statisticService;
    }

    public void ingest(Transaction transaction){
        transactionRepository.save(transaction);
        statisticService.calculateLastSixtySec();
    }


}
