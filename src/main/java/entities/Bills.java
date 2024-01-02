package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bills")
public class Bills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "code")
    private String code;

    @Column(name = "amount_money")
    private double amountMoney;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users user;

    public Bills() {
    }

    public Bills(String type, String code, double amountMoney, Users user) {
        this.type = type;
        this.code = code;
        this.amountMoney = amountMoney;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bills bills = (Bills) o;
        return id == bills.id && Double.compare(bills.amountMoney, amountMoney) == 0 && Objects.equals(type, bills.type) && Objects.equals(code, bills.code) && Objects.equals(user, bills.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, code, amountMoney, user);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(double amountMoney) {
        this.amountMoney = amountMoney;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}