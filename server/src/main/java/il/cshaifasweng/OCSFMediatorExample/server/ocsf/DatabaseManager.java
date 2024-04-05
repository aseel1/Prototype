package il.cshaifasweng.OCSFMediatorExample.server.ocsf;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date; // this is for the date format
import java.util.List;
import java.util.Random;

import il.cshaifasweng.OCSFMediatorExample.entities.Notification;
import il.cshaifasweng.OCSFMediatorExample.entities.SOS;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class DatabaseManager {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public DatabaseManager() {
        // Schedule calculateTimeDiff to run every 6 seconds
        scheduler.scheduleAtFixedRate(this::calculateTimeDiff, 0, 6, TimeUnit.SECONDS);
    }
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() throws HibernateException {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Task.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(SOS.class);
            configuration.addAnnotatedClass(Notification.class);
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
           // String role = (i % 2 == 0) ? "Manager" : "Regular"; // This is just an example, adjust the logic as needed
            String communityManager=null;
            String role = (i % 2 == 0) ? "Manager" : "Regular"; // This is just an example, adjust the logic as needed
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
        User user4 = new User(1234567, "samih", "male", "123", "20", "Nazareth", "manager","Haifa");
        User user5 = new User(1111, "mary", "female", "123", "20", "Cana", "manager","Cana");

        User user6 = new User(2222, "user1", "female", "123", "21", "Cana", "user"," ");



        session.save(user1);
        session.save(user2);
        session.save(user3);
        session.save(user4);
        session.save(user5);
        session.save(user6);
        session.clear();
    }

    public static void generateTasks(Session session) throws Exception {
        Random random = new Random();
        Random randomUser = new Random();
        List<User> users = getAllUsers(session);
        for (int i = 0; i < 10; i++) {
            User user = users.get(randomUser.nextInt(15));//session.get(User.class, random.nextInt(10));
            User volunteer = null;
            System.out.println(user);
            String status;
            if(i%3==0) {
                status="pending";
            } else if (i%3==1) {
                status="done";
            }
            else {
                status="idle";
            }
            if(status.equals("done"))
                volunteer = users.get(randomUser.nextInt(15));//session.get(User.class, random.nextInt(10)); // Assuming there are 10 users
//            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            LocalDateTime now = LocalDateTime.now().withNano(0);
            int time = now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond();//random.nextInt(24); // Assuming time is in hours
            Task task = new Task(i, "Task" + i, now, time, volunteer, status,user);
            if(status.equals("done")) {
                task.setVolTime(now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond() + randomUser.nextInt(15));
                task.setVolDate(now);
            }
            session.save(task);
        }
        session.clear();
    }

    public static void generateSOS(Session session) throws Exception{
        SOS mySos = new SOS();
        session.save(mySos);
        session.clear();
    }
    public static void generateNotifications(Session session) throws Exception{
        Random random = new Random();
        List<User> user = getAllUsers(session);
        for (int i = 0; i < 10; i++) {
            User sender = user.get(15); // Assuming there are 10 users
            User receiver = user.get(14);
            Notification notification = new Notification(sender,receiver,"hi");
            session.save(notification);
        }
        Notification notificationA = new Notification(user.get(5),user.get(6),"is it up?");
        session.save(notificationA);
        session.clear();
    }


    public static List<Task> getTasksByStatusAndCommunity(Session session, String status, String community) {
        List<Task> tasks = null;
        try {
            // Update the query to correctly navigate from Task to its User, then filter by the user's community.
            // This assumes your User entity has a 'community' attribute or a way to identify the user's community.
            // Adjust "user.community" to the correct path from User to the community attribute.
            String hql = "SELECT t FROM Task t WHERE t.status = :status AND t.user.community = :community";
            Query<Task> query = session.createQuery(hql, Task.class);
            query.setParameter("status", status);
            query.setParameter("community", community);
            tasks = query.list();
            System.out.println("Found " + tasks.size() + " tasks with status " + status + " in community " + community + ".");
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    public static List<Task> getTasksDone(Session session, String status, String community) {
        List<Task> tasks = null;
        try {
            String hql = "SELECT t FROM Task t WHERE t.status = :status AND t.volunteer.community = :community";
            Query<Task> query = session.createQuery(hql, Task.class);
            query.setParameter("status", status);
            query.setParameter("community", community);
            tasks = query.list();
            System.out.println("Found " + tasks.size() + " tasks with status " + status + " in community " + community + ".");
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    public static List<SOS> getSOSBetweenDates(Session session, String startDate, String endDate) {
        String hql = "FROM SOS s WHERE s.date BETWEEN :startDate AND :endDate";
        System.out.println(hql);
        return session.createQuery(hql, SOS.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
    }

    public static List<SOS> getSOSByCommunityAndDates(Session session, String community, String startDate, String endDate) {
        String hql = "FROM SOS s WHERE s.user.community = :community AND s.date BETWEEN :startDate AND :endDate";
        return session.createQuery(hql, SOS.class)
                .setParameter("community", community)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .list();
    }

// In DatabaseManager.java


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
    public static void updateNotification(Session session, Notification notification) {
        // Check if the task object has a primary key

        if (notification != null) {
            // The task object is in the detached state, update it
            session.update(notification);
            session.flush(); // Manually flush the session

        }
    }

    public static List<Notification> getAllNotifications(Session session) {
        List<Notification> notifications = null;

        try {
            // Get all tasks
            notifications = session.createQuery("from Notification ").list();
            System.out.println("The notifications list has " + notifications.size() + " notification.");

        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return notifications;
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
    public static List<Task> getTasksByStatusAndUser(Session session, User thisUser) {
        List<Task> tasks = null;
        try {
            // Update the query to correctly navigate from Task to its User, then filter by the user's community.
            // This assumes your User entity has a 'community' attribute or a way to identify the user's community.
            // Adjust "user.community" to the correct path from User to the community attribute.
            String hql = "SELECT t FROM Task t WHERE t.status = :status AND not t.user = :thisUser";
            Query<Task> query = session.createQuery(hql, Task.class);
            query.setParameter("status", "idle");
            query.setParameter("thisUser", thisUser);
            tasks = query.list();
            System.out.println("Found " + tasks.size() + " tasks with status idle " + " and not with user " + thisUser + ".");
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    public static List<Notification> getUsersNotifications(Session session, User user) {
        List<Notification> notifications = new ArrayList<>();

        try {
            String hql = "SELECT n FROM Notification n WHERE (n.recipient = :user OR n.recipient IS NULL) AND n.sender <> :user";
            Query<Notification> query = session.createQuery(hql, Notification.class);
            query.setParameter("user", user);

            notifications = query.list();
            System.out.println("Notifications list has: " + notifications.size() + " notifications.");

        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return notifications;
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
        System.out.println(task.getUser().getUserName());
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
            generateNotifications(session);

            session.getTransaction().commit();

//            printAllUsers(session);
//            printAllTasks(session);
//            getAllTasks(session);
//            getAllUsers(session);
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
            //please do not change this two comments
            new DatabaseManager();

        }
    }

    public void calculateTimeDiff() {
        List<Task> tasks = null;
        List<Notification> notifications = null;
        Session session = DatabaseManager.getSessionFactory().openSession();
        System.out.println("opened session");
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            System.err.println("its been a 6 seconds");
            tasks = getAllTasks(session);
            notifications = getAllNotifications(session);
            // Your time difference calculation logic here
//            LocalDateTime specifiedDate = LocalDateTime.parse(tasks.get(0).getDate());
//            LocalDateTime now = LocalDateTime.now().withNano(0);
//            System.out.println(now);
//            long minutesPassed = ChronoUnit.MINUTES.between(specifiedDate, now);
//            System.out.println("Minutes passed since the specified date: " + minutesPassed);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null)
                tx.rollback();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the session
            if (session != null)
                session.close();
//            System.out.println("closed session");
        }
//        Notification notificationUp = null;
        if(tasks!=null) {
            System.err.println(tasks.get(0).getTaskName());
            for (Task task : tasks) {
//                if (task.getStatus().equals("idle") && task.getVolunteer()==null) {
                if (task.getStatus().equals("pending")) {
                    long minutesPassed = ChronoUnit.MINUTES.between(task.getDate(), LocalDateTime.now().withNano(0));
                    System.out.println(minutesPassed);
                    if(minutesPassed>=1) {
                        System.err.println(minutesPassed);
                        for (Notification notification : notifications) {
//                            System.out.println(
//                                    "Sender: " + notification.getSender().getId() + ", " +
//                                            "User: " + task.getUser().getId() + ", " +
//                                            "Timestamp: " + notification.getTimestamp() + ", " +
//                                            "Date: " + task.getDate()
//                            );
                            if(notification.getRecipient()==null && notification.getSender().getId()==task.getUser().getId() &&
                                    (notification.getTimestamp().equals(task.getDate()))) {
//                            notification.getMessage().equals("Id="+task.getTaskId())){
                                //choose how to idintify the notifivcation...
                                //dont foreget to change back the pending-->idle
                                System.err.println("*******************************************found ittt!!");
//                                System.err.println(LocalDateTime.now().withNano(0));
                                //this didnt update the table :/
                                notification.setTimestamp(LocalDateTime.now().withNano(0));
//                                updateNotification(session, notification);
                            }
                        }
                    }
                }
            }
        }
        else
            System.err.println("there is no tasks");
    }
//    public static void calculateTimeDiff(LocalDateTime specifiedDate){
//        LocalDateTime now = LocalDateTime.now().withNano(0);
//        long minutesPassed = ChronoUnit.MINUTES.between(specifiedDate, now);
//    }
}