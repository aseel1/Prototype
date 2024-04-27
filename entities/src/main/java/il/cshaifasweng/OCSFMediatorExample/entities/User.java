package il.cshaifasweng.OCSFMediatorExample.entities;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    private String status;
    private String gender;
   // private String password;
    private String age;
    private String community;
    private String communityManager;
    private String passwordHash;
    private String passwordSalt;

    @NotNull
    private boolean loggedIn = false;

    public boolean isTaskListOpen() {
        return taskListOpen;
    }

    public void setTaskListOpen(boolean taskListOpen) {
        this.taskListOpen = taskListOpen;
    }

    private boolean taskListOpen = false;

    @ElementCollection
    // @Column(name = "notification")
    private List<String> notifications;

    public User() {
    }

    public User(int id, String userName, String gender, String password, String age, String community, String status,
            String communityManager) {
        super();
        this.id = id;
        this.status = status;
        this.userName = userName;
        this.gender = gender;
        setPassword(password);
        this.age = age;
        this.community = community;
        this.communityManager = communityManager;
        this.notifications = new ArrayList<>();
    }

//    public User(String userName, String password) {
//
//        this.userName = userName;
//        this.password = password;
//
//    }
    public void setPassword(String password) {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Store the salt as a Base64 encoded string
        this.passwordSalt = Base64.getEncoder().encodeToString(salt);

        // Hash the password with the salt
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Store the hashed password as a Base64 encoded string
            this.passwordHash = Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle error appropriately
        }
    }
    public boolean verifyPassword(String password) {
        try {
            // Decode the stored salt
            byte[] salt = Base64.getDecoder().decode(this.passwordSalt);

            // Hash the input password with the stored salt
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Compare the hashed passwords
            String inputHash = Base64.getEncoder().encodeToString(hashedPassword);
            return inputHash.equals(this.passwordHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle error appropriately
            return false;
        }
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPasswordHash() {
        return passwordHash;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getCommunityManager() {
        return communityManager;
    }

    public void setCommunityManager(String communityManager) {
        this.communityManager = communityManager;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    // Method to add a notification
    public void addNotification(String notification) {
        if (this.notifications == null) {
            this.notifications = new ArrayList<>();
        }
        this.notifications.add(notification);
    }

    // Method to remove a notification
    public void removeNotification(String notification) {
        if (this.notifications != null) {
            this.notifications.remove(notification);
        }
    }
}