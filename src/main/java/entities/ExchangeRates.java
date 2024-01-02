package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "date")
    private String date;

    @Column(name = "code_sell")
    private String codeSell;

    @Column(name = "code_buy")
    private String codeBuy;

    @Column(name = "exchange_rate")
    private double exchangeRate;


    public ExchangeRates() { }

    public ExchangeRates(String date, String codeSell, String codeBuy, double exchangeRate) {
        this.date = date;
        this.codeSell = codeSell;
        this.codeBuy = codeBuy;
        this.exchangeRate = exchangeRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRates that = (ExchangeRates) o;
        return id == that.id && Double.compare(that.exchangeRate, exchangeRate) == 0 && Objects.equals(date, that.date) && Objects.equals(codeSell, that.codeSell) && Objects.equals(codeBuy, that.codeBuy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, codeSell, codeBuy, exchangeRate);
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCodeSell() {
        return codeSell;
    }

    public void setCodeSell(String codeSell) {
        this.codeSell = codeSell;
    }

    public String getCodeBuy() {
        return codeBuy;
    }

    public void setCodeBuy(String codeBuy) {
        this.codeBuy = codeBuy;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}