package il.cshaifasweng.OCSFMediatorExample.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

@Entity
@Table(name = "soss")
public class SOS implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOSid", updatable = false)
    private int SOSid;
    private String date;
    private int time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SOS() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = now.format(dateFormatter);
        this.time = now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond();
    }

    public SOS(User user) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = now.format(dateFormatter);
        this.time = now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond();
        this.user = user;
    }

    public int getSOSid() {
        return SOSid;
    }

    public void setSOSid(int SOSid) {
        this.SOSid = SOSid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
