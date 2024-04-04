package il.cshaifasweng.OCSFMediatorExample.server.ocsf;

import java.text.SimpleDateFormat;
import java.util.Date; // this is for the date format
import java.util.List;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import il.cshaifasweng.OCSFMediatorExample.entities.SOS;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;

public class DatabaseManager {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() throws HibernateException {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Task.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(SOS.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    // You can add methods to generate and print Tasks and Users here

    public static String selectRandomString(String... strings) {
        Random random = new Random();
        int index = random.nextInt(strings.length);
        return strings[index];
    }

    public static void generateUsers(Session session) throws Exception {
        Random random = new Random();

        for (int i = 0; i < 14; i++) {
            String role = (i % 2 == 0) ? "Manager" : "Regular"; // This is just an example, adjust the logic as needed
            String communityManager=null;
            if (role.equals("Manager")){
                communityManager=selectRandomString("Haifa", "Nazareth");
            }
            User user = new User(i, "User" + i, "Male", "password" + random.nextInt(),
                    Integer.toString(20 + random.nextInt(60)), selectRandomString("Haifa", "Nazareth"), role,communityManager);
            session.save(user);
        }
        User user1 = new User(212393532, "aseel", "male", "1234", "20", "Haifa", "manager","Nazareth");
        User user2 = new User(2345, "nawal", "female", "1234", "20", "Haifa", "manager","Haifa");
        User user3 = new User(76543, "maya", "female", "1234", "20", "Nazareth", "manager","Nazareth");
        User user4 = new User(1234567, "same7", "male", "123", "20", "Nazareth", "manager","Haifa");

        session.save(user1);
        session.save(user2);
        session.save(user3);
        session.save(user4);
        session.clear();
    }

    public static void generateTasks(Session session) throws Exception {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User volunteer = session.get(User.class, random.nextInt(10)); // Assuming there are 10 users
            String status = "idle"; // You need to implement this method
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int time = random.nextInt(24); // Assuming time is in hours
            Task task = new Task(i, "Task" + i, date, time, volunteer, status);
            session.save(task);
        }
        session.clear();
    }

    public static void generateSOS(Session session) throws Exception{
        SOS mySos = new SOS();
        session.save(mySos);
        session.clear();
    }

    // public static void printAllUsers(Session session) throws Exception {
    // CriteriaBuilder builder = session.getCriteriaBuilder();
    // CriteriaQuery<User> query = builder.createQuery(User.class);
    // query.from(User.class);
    // List<User> data = session.createQuery(query).getResultList();
    // for (User user : data) {
    // System.out.println("ID: " + user.getId() + ", Username: " +
    // user.getUserName() + ", Gender: "
    // + user.getGender() + ", Password: " + user.getPassword() + ", Age: " +
    // user.getAge()
    // + ", Community: " + user.getCommunity());
    // }
    // }

    // public static void printAllTasks(Session session) throws Exception {
    // CriteriaBuilder builder = session.getCriteriaBuilder();
    // CriteriaQuery<Task> query = builder.createQuery(Task.class);
    // query.from(Task.class);
    // List<Task> data = session.createQuery(query).getResultList();
    // for (Task task : data) {
    // System.out.println("ID: " + task.getTaskId());
    // System.out.println("Status: " + task.getStatus());
    // System.out.println(
    // "Volunteer: " + (task.getVolunteer() != null ?
    // task.getVolunteer().getUserName() : "None"));
    // System.out.println("-------------------");
    // }
    // }

    public static void updateTask(Session session, Task task) {
        // Check if the task object has a primary key

        if (task.getTaskId() != 0) {
            // The task object is in the detached state, update it

            session.update(task);
            session.flush(); // Manually flush the session

            System.err.println("hello");
        } else {
            // The task object is in the transient state, save it
            System.out.println("Bye");
            session.save(task);
        }
    }

    public static List<Task> getAllTasks(Session session) {
        List<Task> tasks = null;

        try {
            // Get all tasks
            tasks = session.createQuery("from Task").list();
            System.out.println("The tasks list has " + tasks.size() + " tasks.");

        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static List<User> getAllUsers(Session session) {
        List<User> users = null;

        try {
            // Get all users
            users = session.createQuery("from User").list();
            System.out.println("The users list has aseel   " + users.size() + " users.");

        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static List<User> getAllUsersByCommunity(Session session, String communityName) {
        List<User> users = null;

        try {
            // Get all users by community
            Query<User> query = session.createQuery("SELECT u FROM User u WHERE u.community = :communityName", User.class);
            query.setParameter("communityName", communityName);
            users = query.list();
            System.out.println("The users list has " + users.size() + " users in community " + communityName + ".");
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void addTask(Task task, Session session) {

        session.save(task);
    }

    public static void addUser(Session session, User user) {
        session.save(user);
    }

    public static void addSOS(Session session, SOS sos) {session.save(sos); }

    public static User authenticateUser(User user, Session session) {
        User userFromDB = null;

        try {
            // Create a query to find the user with the provided username and password
            Query query = session.createQuery("FROM User WHERE userName = :username AND password = :password");
            query.setParameter("username", user.getUserName());
            query.setParameter("password", user.getPassword());

            // Execute the query and get the result
            userFromDB = (User) query.uniqueResult();
        } catch (HibernateException e) {
            // handle exception
        }

        return userFromDB;
    }

    public static void initialize() {
        Session session = null;

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            generateUsers(session);
            generateTasks(session);
            generateSOS(session);

            session.getTransaction().commit();

            // printAllUsers(session);
            // printAllTasks(session);
            getAllTasks(session);
            getAllUsers(session);
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