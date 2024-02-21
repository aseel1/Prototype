package il.cshaifasweng.OCSFMediatorExample.server.ocsf;

import java.text.SimpleDateFormat;
import java.util.Date; // this is for the date format
import java.util.List;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;

public class DatabaseManager {
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Task.class);
        configuration.addAnnotatedClass(User.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    // You can add methods to generate and print Tasks and Users here

    public static void generateUsers() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User user = new User(i, "User" + i, "Male", "password" + random.nextInt(),
                    Integer.toString(20 + random.nextInt(60)), "Community" + random.nextInt(10));
            session.save(user);
            session.flush();
        }
    }

    public static void generateTasks() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User volunteer = session.get(User.class, random.nextInt(10)); // Assuming there are 10 users
            String status = "idle"; // You need to implement this method
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int time = random.nextInt(24); // Assuming time is in hours
            Task task = new Task(i, "Task" + i, date, time, volunteer, status);
            session.save(task);
            session.flush();
        }
    }

    public static void printAllUsers() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        query.from(User.class);
        List<User> data = session.createQuery(query).getResultList();
        for (User user : data) {
            System.out.println("ID: " + user.getId() + ", Username: " + user.getUserName() + ", Gender: "
                    + user.getGender() + ", Password: " + user.getPassword() + ", Age: " + user.getAge()
                    + ", Community: " + user.getCommunity());
        }
    }

    public static void printAllTasks() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        query.from(Task.class);
        List<Task> data = session.createQuery(query).getResultList();
        for (Task task : data) {
            System.out.println("ID: " + task.getTaskId());
            System.out.println("Status: " + task.getStatus());
            System.out.println(
                    "Volunteer: " + (task.getVolunteer() != null ? task.getVolunteer().getUserName() : "None"));
            System.out.println("-------------------");
        }
    }

    public static void initialize() {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            generateUsers();
            generateTasks();
            printAllUsers();
            printAllTasks();
            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
        }
    }
}