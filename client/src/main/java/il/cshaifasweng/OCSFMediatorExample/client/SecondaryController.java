package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.message;

public class SecondaryController {

    // !private ObservableList<User> users;

    // !public void setUsers(List<User> users) {
    // this.users = FXCollections.observableArrayList(users);
    // }

    @FXML
    private TableView<User> userTable;

    public TableView<User> getUserTable() {
        return userTable;
    }

    public ObservableList<User> getTableItems() {
        return userTable.getItems();
    }

    List<User> Users1 = new ArrayList<User>();

    @FXML
    private TableColumn<User, Integer> user_id;

    @FXML
    private TableColumn<User, String> user_userName;

    @FXML
    private TableColumn<User, String> user_gender;

    @FXML
    private TableColumn<User, String> user_password;

    @FXML
    private TableColumn<User, String> user_age;

    @FXML
    private TableColumn<User, String> user_community;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    public void initialize() {
        user_id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        user_userName.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        user_gender.setCellValueFactory(new PropertyValueFactory<User, String>("gender"));
        user_password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        user_age.setCellValueFactory(new PropertyValueFactory<User, String>("age"));
        user_community.setCellValueFactory(new PropertyValueFactory<User, String>("community"));

        ObservableList<User> observableUsers = FXCollections.observableArrayList((List<User>) message.getObject());
        System.out.println("Created ObservableList with " + observableUsers.size() +
                " users.");
        userTable.setItems(observableUsers);

        System.out.println("Initialized TableView with " + userTable.getColumns().size() + " columns.");
        System.out.println("In initialize. userTable is " + (userTable == null ? "null" : "not null"));

    }

    // public void updateTable(List<User> users) {
    // System.out.println("Updating table with " + users.size() + " users.");
    // System.out.println("Number of columns in userTable: " +
    // userTable.getColumns().size());
    // System.out.println("In updateTable. userTable is " + (userTable == null ?
    // "null" : "not null"));

    // ObservableList<User> observableUsers =
    // FXCollections.observableArrayList(users);
    // System.out.println("Created ObservableList with " + observableUsers.size() +
    // " users.");

    // userTable.setItems(observableUsers);
    // userTable.refresh();

    // System.out.println("Set items in userTable. Number of items: " +
    // userTable.getItems().size());
    // }

}