package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Notification;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TaskFormController {

    @FXML // fx:id="MenuBtn"
    private MenuButton MenuBtn; // Value injected by FXMLLoader

    @FXML // fx:id="Specification"
    private TextField Specification; // Value injected by FXMLLoader

    @FXML // fx:id="Task1"
    private MenuItem Task1; // Value injected by FXMLLoader

    @FXML // fx:id="task2"
    private MenuItem task2; // Value injected by FXMLLoader

    @FXML // fx:id="task3"
    private MenuItem task3; // Value injected by FXMLLoader

    @FXML // fx:id="other"
    private MenuItem other; // Value injected by FXMLLoader




    @FXML
    private TextField statusField;

    @FXML
    private Button submitButton;

    @FXML
    private TextField taskNameField;

//    @FXML
//    private TextField timeField;

    @FXML
    private TextField volunteerField;

    private Task task;
    private String taskPicked= "";


    @FXML
    void otherTask(ActionEvent event) {
        taskPicked= "Other";
    }

    @FXML
    void picktask1(ActionEvent event) {
        taskPicked= "Walk The Dog";
    }

    @FXML
    void picktask2(ActionEvent event) {
        taskPicked= "Buying Medicine";
    }

    @FXML
    void picktask3(ActionEvent event) {
        taskPicked= "Get a Ride";
    }


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
        LocalDateTime now = LocalDateTime.now().withNano(0);
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        task.setDate(dateField.getText().isEmpty() ? now.format(dateFormatter) : dateField.getText());
//        task.setTime(timeField.getText().isEmpty() ? now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond()
//                : Integer.parseInt(timeField.getText()));
        task.setDate(now);
//        task.setTime(timeField.getText().isEmpty() ? now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond()
//                : Integer.parseInt(timeField.getText()));

        task.setUser(SimpleClient.getCurrentUser());
        task.setVolunteer(null); //no volunteer yet!
        task.setStatus("pending");
        String str1= Specification.getText().isEmpty() ? "" : Specification.getText();
        String details= taskPicked+"-"+str1;
        task.setDetails(details);
        Message message = new Message("#submitTask", task);
        System.out.println("received");
        System.out.println(task.getTaskName() + " " + task.getDate() + " "
                + task.getUser().getUserName()
                + task.getUser().getAge()
                + task.getUser().getGender()
                + task.getUser().getCommunity()
                + task.getUser().getPassword()
                + " " + task.getStatus());
        try {
            SimpleClient.getClient().sendToServer(message);
            System.out.println("(Task from client) sent task request");
            //send to all users
        } catch (IOException e) {
            e.printStackTrace();
        }
        //hello marry uncomment this one and use it
        // receiver_id = -1 if its for all
        // SimpleClient.sendNotification(SimpleClient.getCurrentUser(),receiver_id,"message");
        //empty the textfields:
//        timeField.setText("");
        Specification.setText("");
        taskNameField.setText("");

    }

    public void handlePressingSOS(ActionEvent event) {
        SimpleClient.pressingSOS("TaskForm");
    }
}
