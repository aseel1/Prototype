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

public class MembersController {
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

        ObservableList<User> observableUsers = FXCollections.observableArrayList((List<User>) message.getObject());
        System.out.println("Created ObservableList with " + observableUsers.size() +
                " users.");
        userTable.setItems(observableUsers);

        System.out.println("Initialized TableView with " + userTable.getColumns().size() + " columns.");
        System.out.println("In initialize. userTable is " + (userTable == null ? "null" : "not null"));

    }
}
