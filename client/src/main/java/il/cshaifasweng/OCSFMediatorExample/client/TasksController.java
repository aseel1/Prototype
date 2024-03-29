package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.message;

import java.io.IOException;
import java.util.List;

public class TasksController {

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> taskId;

    @FXML
    private TableColumn<Task, String> taskName;

    @FXML
    private TableColumn<Task, String> date;

    @FXML
    private TableColumn<Task, Integer> time;

    @FXML
    private TableColumn<Task, User> volunteer;

    @FXML
    private TableColumn<Task, String> status;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    public void initialize() {
        taskId.setCellValueFactory(new PropertyValueFactory<Task, Integer>("taskId"));
        taskName.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
        date.setCellValueFactory(new PropertyValueFactory<Task, String>("date"));
        time.setCellValueFactory(new PropertyValueFactory<Task, Integer>("time"));
        volunteer.setCellValueFactory(new PropertyValueFactory<Task, User>("volunteer"));
        status.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));

        ObservableList<Task> observableTasks = FXCollections.observableArrayList((List<Task>) message.getObject());
        taskTable.setItems(observableTasks);

        taskTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showTaskDetails(newSelection);
            }
        });

    }

    public void showTaskDetails(Task task) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Task Details");
        dialog.setHeaderText("Details of task: " + task.getTaskName());

        // Create labels and fields
        Label label1 = new Label("Task ID: ");
        Label label2 = new Label("Task Name: ");
        Label label3 = new Label("Date: ");
        Label label4 = new Label("Time: ");
        Label label5 = new Label("Volunteer: ");
        Label label6 = new Label("Status: ");

        TextField text1 = new TextField(String.valueOf(task.getTaskId()));
        TextField text2 = new TextField(task.getTaskName());
        TextField text3 = new TextField(task.getDate());
        TextField text4 = new TextField(String.valueOf(task.getTime()));
        TextField text5 = new TextField(task.getVolunteer().toString());
        TextField text6 = new TextField(task.getStatus());

        text1.setEditable(false);
        text2.setEditable(true);
        text3.setEditable(true);
        text4.setEditable(true);
        text5.setEditable(true);
        text6.setEditable(true);

        // Create layout and add items
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(text3, 2, 3);
        grid.add(label4, 1, 4);
        grid.add(text4, 2, 4);
        grid.add(label5, 1, 5);
        grid.add(text5, 2, 5);
        grid.add(label6, 1, 6);
        grid.add(text6, 2, 6);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            return null;
        });

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                task.setTaskId(Integer.parseInt(text1.getText()));
                task.setTaskName(text2.getText());
                task.setDate(text3.getText());
                task.setTime(Integer.parseInt(text4.getText()));

                // You might need to convert the text5.getText() to a User object
                // task.setVolunteer(...);
                task.setStatus(text6.getText());

                // Create a new Message object with the updated task
                Message message = new Message("#updateTask", task);

                // Send the message to the server
                try {
                    SimpleClient.getClient().sendToServer(message);
                    taskTable.refresh();
                    return "Task updated";
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        });

        // Show dialog
        dialog.show();
    }

}