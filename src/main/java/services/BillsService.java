package services;

import entities.Bills;
import entities.Users;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Random;
import java.util.Scanner;

public class BillsService {
    static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("BankingSystem");
    static EntityManager em = emf.createEntityManager();

    public static void createNewBill(Scanner scanner) {
        Random random = new Random();
        int[] cardNums = new int[12];

        for (int i = 0; i < cardNums.length; i++)
            cardNums[i] = random.nextInt(0, 10);
        StringBuilder sb = new StringBuilder();
        for (int j : cardNums)
            sb.append(j);

        String codeVisa = "4441" + sb;
        String codeMasterCard = "5375" + sb;
        double defaultValue = 0.0;

        System.out.println("Enter user name:");
        String nameUser = scanner.nextLine();
        System.out.println("Enter user surname:");
        String surnameUser = scanner.nextLine();
        System.out.print("Enter currency type:\n\t1 - USD\n\t2 - EUR\n\t3 - UAH\n->");
        String currencyType = null;
        switch (scanner.nextLine()) {
            case "1" -> currencyType = "USD";
            case "2" -> currencyType = "EUR";
            case "3" -> currencyType = "UAH";
        }

        System.out.print("Enter payment system:\n\t1 - Visa\n\t2 - MasterCard\n->");
        String paymentSystem = null;
        switch (scanner.nextLine()) {
            case "1" -> paymentSystem = "Visa";
            case "2" -> paymentSystem = "MasterCard";
        }
        String code;

        try {
            Query query = em.createQuery("SELECT u FROM Users u " + "WHERE u.name ='" +
                    nameUser + "' " + "AND u.surname ='" + surnameUser + "'", Users.class);
            Users user = (Users) query.getSingleResult();

            em.getTransaction().begin();
            try {
                if (!(currencyType.equals("USD") || currencyType.equals("EUR") || currencyType.equals("UAH"))) {
                    System.err.println("We do not support this type of currency!");
                    em.getTransaction().rollback();
                    return;
                }

                if (paymentSystem.equals("Visa")) {
                    code = codeVisa;
                } else if (paymentSystem.equals("MasterCard")) {
                    code = codeMasterCard;
                } else {
                    System.err.println("We do not support this payment system!");
                    em.getTransaction().rollback();
                    return;
                }

                Bills bill = new Bills(currencyType, code, defaultValue, user);
                em.persist(bill);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("User not registered!");
        }
    }

    public static void addMoneyToBill(Scanner scanner) {
        System.out.println("Enter card code:");
        String cardCode = scanner.nextLine();
        System.out.println("Enter amount of money:");
        double amountMoney = scanner.nextDouble();

        if (amountMoney <= 0) {
            System.err.println("You need donation more than '0' money to bill");
            return;
        }

        try {
            Query query = em.createQuery("SELECT c FROM entities.Bills c WHERE c.code ='" +
                    cardCode + "'", Bills.class);
            Bills bill = (Bills) query.getSingleResult();

            em.getTransaction().begin();
            try {
                double moneyNow = bill.getAmountMoney();
                double setMoney = amountMoney + moneyNow;
                bill.setAmountMoney(setMoney);
                em.persist(bill);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("User not found!");
        }
    }
}