package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.Notification;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.pressingSOS;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.tableMessage;

public class NotificationsController {

    @FXML
    private TableView<Notification> notificationTableView;

//    @FXML
//    private ListView<String> notificationsList;
//
//    @FXML
//    private Button returnButton;

    @FXML
    private TableColumn<Notification, String> sender;

    @FXML
    private TableColumn<Notification, String> message;

    @FXML
    private void initialize() {
        // You can initialize your ListView or perform other setup here
        System.out.println("Trying to initialize Notifications");
        message.setCellValueFactory(new PropertyValueFactory<Notification, String>("message"));
        sender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSender().getUserName()));

        ObservableList<Notification> observableNotification =
                FXCollections.observableArrayList((List<Notification>) tableMessage.getObject());
        System.out.println("Created ObservableList with " + observableNotification.size() + " notifications.");
        notificationTableView.setItems(observableNotification);
//  idk if we want this
//        notificationTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//                showNotification(newSelection);
//            }
//        });

        System.out.println("Initialized TableView with " + notificationTableView.getColumns().size() + " columns.");
        System.out.println("In initialize. userTable is " + (notificationTableView == null ? "null" : "not null"));

    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary"); // Assuming App is your main application class
    }

}
