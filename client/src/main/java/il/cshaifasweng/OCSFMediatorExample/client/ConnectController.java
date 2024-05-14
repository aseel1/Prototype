package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectController {
    @FXML
    private TextField ipField;

    @FXML
    private void connect() throws IOException {
        String ipAddress = ipField.getText();
        SimpleClient client = SimpleClient.getClient();
        if (ipAddress == "") {
            ipAddress = "wrong";
        } // i made this because it connect to a null string.
        client.setHost(ipAddress);
        try {
            client.openConnection();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.err.println("wrong ip address");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Wrong IP address");
                alert.showAndWait();
            });
        }

        if (client.isConnected()) {
            System.out.println("Client is connected to the server.");
            App.setRoot("Login");
        } else {
            System.out.println("Client failed to connect to the server.");
        }
    }
}