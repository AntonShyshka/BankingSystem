import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

import static services.BillsService.addMoneyToBill;
import static services.BillsService.createNewBill;
import static services.ExchangeService.*;
import static services.TransactionsService.moneyTransaction;
import static services.UsersService.addNewUser;

public class Main {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean close = true;
        try {
            emf = Persistence.createEntityManagerFactory("BankingSystem");
            em = emf.createEntityManager();

            try {
                while (close) {
                    System.out.println("Enter you choose:");
                    System.out.println("\t1: add new user");
                    System.out.println("\t2: create new bill");
                    System.out.println("\t3: add money to bill");
                    System.out.println("\t4: transfer money between bills");
                    System.out.println("\t5: exchange money between own bills");
                    System.out.println("\t6: get total amount of money in UAH in the account");
                    System.out.println("\tEM: add new exchange rates");
                    System.out.println("Press 'E' for exit");
                    System.out.print("->");

                    String s = scanner.nextLine();
                    switch (s) {
                        case "1" -> addNewUser(scanner);
                        case "2" -> createNewBill(scanner);
                        case "3" -> addMoneyToBill(scanner);
                        case "4" -> moneyTransaction(scanner);
                        case "5" -> exchangeBetweenBills(scanner);
                        case "6" -> allMoneyInUah(scanner);
                        case "EM" -> addNewExchangeRates(scanner);
                        case "E" -> close = false;
                    }
                }
            } finally {
                scanner.close();
                emf.close();
                em.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}