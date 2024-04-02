package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.util.Objects;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserCreationFormController {

    @FXML
    private TextField ageField;

    @FXML
    private TextField idField;

    @FXML
    private TextField communityField;

    @FXML
    private TextField communityManagerField;

    @FXML
    private Button createUserButton;

    @FXML
    private TextField genderField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField statusField;

    @FXML
    private TextField usernameField;

    @FXML
    void handleCreateUser(ActionEvent event) {
        int id = Integer.parseInt(idField.getText());
        String username = usernameField.getText();
        String password = passwordField.getText();
        String gender = genderField.getText();
        String age = ageField.getText();
        String community = communityField.getText();
        String status = statusField.getText();
        String communitymanager=null;
        if (Objects.equals(status, "manager")){
            communitymanager = communityManagerField.getText();
        }
        User user = new User(id, username, gender, password, age, community, status, communitymanager);
        Message message = new Message("#createUser", user);

        try {
            SimpleClient.getClient().sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
