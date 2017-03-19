package ve.salc2.challenge.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Double amount;
    private Long timestamp;

    public Transaction() {
    }

    public Transaction(Double amount, Long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
