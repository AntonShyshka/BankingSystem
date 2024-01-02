package services;

import entities.Users;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class UsersService {
    static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("BankingSystem");
    static EntityManager em = emf.createEntityManager();

    public static void addNewUser(Scanner scanner) {
        System.out.println("Enter user name:");
        String name = scanner.nextLine();
        System.out.println("Enter user surname:");
        String surname = scanner.nextLine();

        em.getTransaction().begin();
        try {
            Users user = new Users(name, surname);
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }
}