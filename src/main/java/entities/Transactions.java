package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "currency_type")
    private String currencyType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moveFrom")
    private Bills billFrom;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "moveTo")
    private Bills billTo;

    public Transactions() { }

    public Transactions(String transactionType, String currencyType,
                        Bills billFrom, Bills billTo) {
        this.transactionType = transactionType;
        this.currencyType = currencyType;
        this.billFrom = billFrom;
        this.billTo = billTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transactions that = (Transactions) o;
        return id == that.id && Objects.equals(transactionType, that.transactionType) && Objects.equals(currencyType, that.currencyType) && Objects.equals(billFrom, that.billFrom) && Objects.equals(billTo, that.billTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionType, currencyType, billFrom, billTo);
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Bills getBillFrom() {
        return billFrom;
    }

    public void setBillFrom(Bills billFrom) {
        this.billFrom = billFrom;
    }

    public Bills getBillTo() {
        return billTo;
    }

    public void setBillTo(Bills billTo) {
        this.billTo = billTo;
    }
}