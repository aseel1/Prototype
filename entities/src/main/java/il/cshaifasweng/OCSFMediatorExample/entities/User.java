package il.cshaifasweng.OCSFMediatorExample.entities;
import com.sun.istack.NotNull;
import java.io.Serializable;

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
    private String password;
    private String age;
    private String community;
    private String communityManager;

    @NotNull
    private boolean loggedIn=false;

    public User() {
    }

    public User(int id, String userName, String gender, String password, String age, String community, String status, String communityManager) {
        super();
        this.id = id;
        this.status = status;
        this.userName = userName;
        this.gender = gender;
        this.password = password;
        this.age = age;
        this.community = community;
        this.communityManager=communityManager;
    }

    public User(String userName, String password) {

        this.userName = userName;
        this.password = password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}