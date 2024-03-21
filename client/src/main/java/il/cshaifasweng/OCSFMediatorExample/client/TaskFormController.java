package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TaskFormController {
    @FXML
    private TextField dateField;

    @FXML
    private TextField statusField;

    @FXML
    private Button submitButton;

    @FXML
    private TextField taskNameField;

    @FXML
    private TextField timeField;

    @FXML
    private TextField volunteerField;

    private Task task;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @FXML
    private void submitTask() {

        task = new Task();
        task.setTaskName(taskNameField.getText());
        // Set the date and time to now
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        task.setDate(dateField.getText().isEmpty() ? now.format(dateFormatter) : dateField.getText());
        task.setTime(timeField.getText().isEmpty() ? now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond()
                : Integer.parseInt(timeField.getText()));

        // For the volunteer field, you would need to fetch the User object based on the
        // entered ID
        // This is a placeholder and might not work depending on your User class
        // implementation
        // task.setVolunteer(new User(volunteerField.getText()));
        task.setStatus(statusField.getText());
        Message message = new Message("#submitTask", task);
        System.err.println(task.getTaskName() + " " + task.getDate() + " " + task.getTime() + " " + task.getVolunteer()
                + " " + task.getStatus());
        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
