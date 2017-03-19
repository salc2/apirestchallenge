package ve.salc2.challenge.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    private final long current = System.currentTimeMillis();
    private final long from = current-(60*1000);
    private final long to = current+(60*1000);


    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void findByTimestampBetweenOneFound() throws Exception {

        Transaction transaction = new Transaction(15.55, current);
        repository.save(transaction);

        List<Transaction> transactionsBetween = repository
                .findByTimestampBetween(from, to);

        assertThat(transactionsBetween.size()).isEqualTo(1);
        assertThat(transactionsBetween.get(0).getAmount())
                .isEqualTo(15.55);
    }

    @Test
    public void findByTimestampBetweenEmpty() throws Exception{

        List<Transaction> transactionsBetweenEmpty = repository
                .findByTimestampBetween(to, System.currentTimeMillis());

        assertThat(transactionsBetweenEmpty).isEmpty();
    }

}
