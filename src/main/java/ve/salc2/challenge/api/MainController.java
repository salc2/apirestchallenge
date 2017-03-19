package ve.salc2.challenge.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ve.salc2.challenge.domain.StatisticService;
import ve.salc2.challenge.domain.TransactionService;
import ve.salc2.challenge.domain.Statistic;
import ve.salc2.challenge.domain.Transaction;

@RestController
public class MainController {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private TransactionService transactionService;


    @RequestMapping(value = "/transactions",method = RequestMethod.POST)
    public ResponseEntity createTransaction(@RequestBody Transaction transaction){
        transactionService.ingest(transaction);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/statistics",method = RequestMethod.GET)
    public ResponseEntity<Statistic> lastStatistic(){
        return statisticService.lastSixtySecStatistic()
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(503).build());
    }

}
