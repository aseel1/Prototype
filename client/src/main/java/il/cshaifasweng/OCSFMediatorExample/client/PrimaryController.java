package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.greenrobot.eventbus.EventBus;

import com.mysql.cj.xdevapi.Client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrimaryController {

	@FXML
	void sendWarning(ActionEvent event) {
		try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private Button showUsersButton;

	@FXML
	private Button openTaskButton;

	@FXML
	private Button showTasksButton;

	@FXML
	private Label usernameLabel;

	@FXML
	private Label statusLabel;

	private static PrimaryController instance;

	public PrimaryController() {
		instance = this;
	}

	public static PrimaryController getInstance() {
		return instance;
	}

	public void updateLabels(String username, String status) {
		usernameLabel.setText("Username: " + username);
		statusLabel.setText("Status: " + status);
	}

	@FXML
	protected void handleShowUsersButtonAction(ActionEvent event) {
		Message message = new Message("#showUsersList");
		try {
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary)Sending message to server: ");

		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}

	}

	@FXML
	protected void handleShowTasksButtonAction(ActionEvent event) {
		Message message = new Message("#showTasksListfgsgsdgfs");
		try {
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary)Sending message to server: ");

		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}

	}

	@FXML
	protected void openTaskButtonAction(ActionEvent event) {
		Message message = new Message("#openTask");
		try {
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary)Sending message to server: ");

		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}

	}

}
