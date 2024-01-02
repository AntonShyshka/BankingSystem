package services;

import entities.Bills;
import entities.Transactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Scanner;

public class TransactionsService {
    static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("BankingSystem");
    static EntityManager em = emf.createEntityManager();

    public static void moneyTransaction(Scanner scanner) {
        System.out.println("Enter number of the card from which you want to make a transfer:");
        String cardCodeFrom = scanner.nextLine();
        System.out.println("Enter number of number to which you want to make a transfer:");
        String cardCodeTo = scanner.nextLine();
        System.out.println("Enter how match money you want to transfer:");
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

            em.getTransaction().begin();
            try {
                if (!typeFrom.equals(typeTo)) {
                    System.err.println("The currency code of the accounts does not match");
                    em.getTransaction().rollback();
                }

                moneyBufferFrom = billFrom.getAmountMoney() - money;
                billFrom.setAmountMoney(moneyBufferFrom);

                moneyBufferTo = billTo.getAmountMoney() + money;
                billTo.setAmountMoney(moneyBufferTo);

                Transactions transactions =
                        new Transactions("between_bills", typeFrom, billFrom, billTo);

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
}