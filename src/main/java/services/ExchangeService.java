package services;

import entities.Bills;
import entities.ExchangeRates;
import entities.Transactions;
import entities.Users;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ExchangeService {
    static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("BankingSystem");
    static EntityManager em = emf.createEntityManager();
    public static void addNewExchangeRates(Scanner scanner) {
        Date dateOb = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String data = formatter.format(dateOb);

        System.out.print("Enter code sell->");
        String codeSell = scanner.nextLine();
        System.out.print("Enter code buy->");
        String codeBuy = scanner.nextLine();
        System.out.print("Enter exchange rate->");
        double exchangeRate = scanner.nextDouble();

        em.getTransaction().begin();
        try {
            ExchangeRates er = new ExchangeRates(data, codeSell, codeBuy, exchangeRate);

            em.persist(er);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }
    public static void exchangeBetweenBills(Scanner scanner) {
        System.out.println("Enter number of the card from which you want to make a exchange:");
        String cardCodeFrom = scanner.nextLine();
        System.out.println("Enter number of number to which you want to make a exchange:");
        String cardCodeTo = scanner.nextLine();
        System.out.println("Enter how match money you want to exchange:");
        double money = scanner.nextDouble();
        double moneyBufferFrom;
        double moneyBufferTo;

        try {
            Query queryFrom = em.createQuery("SELECT b FROM Bills b WHERE b.code = '" +
                    cardCodeFrom + "'", Bills.class);
            Query queryTo = em.createQuery("SELECT b FROM Bills b WHERE b.code = '" +
                    cardCodeTo + "'", Bills.class);

            Bills billFrom = (Bills) queryFrom.getSingleResult();
            Bills billTo = (Bills) queryTo.getSingleResult();

            String typeFrom = billFrom.getType();
            String typeTo = billTo.getType();

            Query queryCurrencyType = em.createQuery("SELECT t FROM ExchangeRates t WHERE t.codeBuy = '" +
                    typeTo + "' AND t.codeSell = '" + typeFrom + "'", ExchangeRates.class);

            ExchangeRates exchangeRates = (ExchangeRates) queryCurrencyType.getSingleResult();

            em.getTransaction().begin();
            try {
                if (billFrom.getUser() != billTo.getUser()) {
                    System.err.println("You can do this operation only between you`r bills!");
                    return;
                }

                if (typeFrom.equals(typeTo)) {
                    moneyBufferFrom = billFrom.getAmountMoney() - money;
                    billFrom.setAmountMoney(moneyBufferFrom);

                    moneyBufferTo = billTo.getAmountMoney() + money;
                    billTo.setAmountMoney(moneyBufferTo);
                } else {
                    moneyBufferFrom = billFrom.getAmountMoney() - money;
                    billFrom.setAmountMoney(moneyBufferFrom);

                    moneyBufferTo = billTo.getAmountMoney() + money *
                            exchangeRates.getExchangeRate();
                    billTo.setAmountMoney(moneyBufferTo);
                }


                Transactions transactions =
                        new Transactions("exchange_between_bills", typeFrom, billFrom, billTo);

                em.persist(transactions);
                em.persist(billFrom);
                em.persist(billTo);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            System.err.println("Bill not found");
        }
    }

    public static void allMoneyInUah(Scanner scanner) {
        System.out.println("Enter you`r name:");
        String name = scanner.nextLine();
        System.out.println("Enter you`r surname:");
        String surname = scanner.nextLine();
        double moneyCounter = 0;
        try {
            Query query = em.createQuery("SELECT u FROM Users u WHERE u.name = '" + name +
                    "' AND u.surname = '" + surname + "'");

            Users user = (Users) query.getSingleResult();
            List<Bills> userBills = user.getBills();

            for (Bills bill : userBills) {
                if (bill.getType().equals("UAH")) {
                    moneyCounter += bill.getAmountMoney();
                } else {
                    String type = bill.getType();

                    Query queryType = em.createQuery("SELECT t FROM ExchangeRates t " +
                            "WHERE t.codeSell = '" + type + "' AND t.codeBuy = 'UAH'");

                    ExchangeRates er = (ExchangeRates) queryType.getSingleResult();

                    double rate = er.getExchangeRate();

                    moneyCounter += bill.getAmountMoney() * rate;
                }
            }
            System.out.println("The total amount of money in UAH in the account: " + moneyCounter);
        }catch (Exception e) {
            System.err.println("Client not found");
            e.printStackTrace();
        }
    }
}