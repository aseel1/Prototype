package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "Tasks")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId", updatable = false)
    private int taskId;
    private String taskName;
    private String date;
    private int time;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private User volunteer;
    private String status;

    public Task() {
    }

    public Task(int taskId, String taskName, String date, int time, User volunteer, String status) {
        super();
        this.taskId = taskId;
        this.taskName = taskName;
        this.date = date;
        this.time = time;
        this.volunteer = volunteer;
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public User getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(User volunteer) {
        this.volunteer = volunteer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}