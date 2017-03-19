package ve.salc2.challenge.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByTimestampBetween(Long min, Long max);
}