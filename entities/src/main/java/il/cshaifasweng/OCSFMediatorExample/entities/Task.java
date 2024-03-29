package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

import javax.persistence.*;

// import net.bytebuddy.jar.asm.Label;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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

    public void displayTaskForm(Task task) {
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(100);
        grid.setVgap(100);
        grid.setPadding(new Insets(10, 10, 10, 10));

        TextField taskNameField = new TextField();
        grid.add(new Label("Task Name:"), 0, 0);
        grid.add(taskNameField, 1, 0);

        TextField dateField = new TextField();
        grid.add(new Label("Date:"), 0, 1);
        grid.add(dateField, 1, 1);

        TextField timeField = new TextField();
        grid.add(new Label("Time:"), 0, 2);
        grid.add(timeField, 1, 2);

        TextField volunteerField = new TextField();
        grid.add(new Label("Volunteer:"), 0, 3);
        grid.add(volunteerField, 1, 3);

        TextField statusField = new TextField();
        grid.add(new Label("Status:"), 0, 4);
        grid.add(statusField, 1, 4);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            task.setTaskName(taskNameField.getText());
            task.setDate(dateField.getText());
            task.setTime(Integer.parseInt(timeField.getText()));
            // For the volunteer field, you would need to fetch the User object based on the
            // entered ID
            // This is a placeholder and might not work depending on your User class
            // implementation
            // task.setVolunteer(new User(volunteerField.getText()));
            task.setStatus(statusField.getText());

            stage.close(); // close the form
        });
        grid.add(submitButton, 1, 5);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void saveTaskToDatabase(Task task) {
        // Save the task to the database
    }

}