package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Notification;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.*;

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

        //subscribe this fxml page to eventBus
        EventBusManager.getEventBus().register(this);
        if(EventBusManager.getEventBus().isRegistered(this)){
            System.out.println("FXML NotificationController registered to event bus");
        }

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
        notificationTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showNotification(newSelection);
            }
        });

        System.out.println("Initialized TableView with " + notificationTableView.getColumns().size() + " columns.");
        System.out.println("In initialize. userTable is " + (notificationTableView == null ? "null" : "not null"));

    }
    private void showNotification(Notification notification) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification Details");
        alert.setHeaderText(null);
        alert.setContentText(notification.getMessage());
        alert.showAndWait();
        Platform.runLater(() -> notificationTableView.getSelectionModel().clearSelection());


    }

    // Define methods to handle events posted on the EventBus
    @Subscribe
    public void onNotificationReceived(NotificationReceivedEvent event) {
        System.out.println("(NotificationController) event received by "+getCurrentUser().getUserName());
        // Handle the new notification event
        Notification newNotification = event.getNewNotification();
        //check if notification is for this User:
        //System.out.println("(NotificationController) recipient is: "+ newNotification.getRecipient().getUserName());
        if(newNotification.getRecipient()==null || newNotification.getRecipient().getId()==getCurrentUser().getId()) {
            System.out.println("(NotificationController) currentUser is indeed recipient");
            //create a new table in order to add the new Item:
            ObservableList<Notification> observableNotification = FXCollections.observableArrayList((List<Notification>) tableMessage.getObject());
            // Add the newNotification to the observableNotification list
            observableNotification.add(newNotification);
            // Set the updated observableNotification list as the items of the notificationTableView
            notificationTableView.setItems(observableNotification);
        }
    }

    @FXML
    private void switchToPrimary() throws IOException {
        //unsubscribe from eventBus:
        EventBusManager.getEventBus().unregister(this);
        App.setRoot("primary"); // Assuming App is your main application class
    }
    public void handlePressingSOS(ActionEvent event) {
        SimpleClient.pressingSOS("Notifications");
    }

}
