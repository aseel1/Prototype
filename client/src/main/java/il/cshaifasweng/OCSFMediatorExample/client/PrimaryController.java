package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
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

	@FXML // fx:id="chngPic"
	private Button chngPic; // Value injected by FXMLLoader

	@FXML // fx:id="userPic"
	private ImageView userPic; // Value injected by FXMLLoader


	@FXML
	private Button showUsersButton;

	@FXML
	private Button openTaskButton;

	@FXML
	private Button showTasksButton;

	@FXML
	private Button myTasksButton;

	@FXML
	private Label usernameLabel;

	@FXML
	private Label statusLabel;

	@FXML
	private Button SOSReports;

	@FXML
	private MenuButton reportsButton;

	private static PrimaryController instance;

	public PrimaryController() {
		instance = this;
	}

	@FXML
	public void initialize() {
		User currentUser = SimpleClient.getCurrentUser();
		updateLabels(currentUser.getUserName(), currentUser.getStatus());
		showUsersButton.setVisible(false);
	}

	public static PrimaryController getInstance() {
		return instance;
	}

	public void updateLabels(String username, String status) {
		usernameLabel.setText("Username: " + username);
		statusLabel.setText("Status: " + status);
		//setting image:
		File file= SimpleClient.getCurrentUser().getImageFile();
		Image image = new Image(file.toURI().toString());
		userPic.setImage(image);

		reportsButton.setVisible("manager".equals(status) || "Manager".equals(status));
		SOSReports.setVisible("manager".equals(status) || "Manager".equals(status));
	}

	@FXML
	void changePic(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Profile Picture");
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			Image image = new Image(selectedFile.toURI().toString());
			userPic.setImage(image);
			//set userPic in database too
			Message message = new Message("#updatePic", SimpleClient.getCurrentUser(), selectedFile);
			System.out.println(message);
			try {
				System.out.println(message.getMessage());
				SimpleClient.getClient().sendToServer(message);
				System.out.println("(Primary) Sending req message to server.");
			} catch (IOException e) {
				System.out.println("Failed to connect to the server.");
				e.printStackTrace();
			}
		}
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
		Message message = new Message("#showTasksList", SimpleClient.getCurrentUser());
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

	@FXML
	protected void logOutAction(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES,
				ButtonType.NO);
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.YES) {
				sendLogoutRequest();
			}
		});
	}

	private void sendLogoutRequest() {

		Message message = new Message("#LogOut", SimpleClient.getCurrentUser());
		System.out.println(message);
		try {
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary) Sending logout message to server.");
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}
	}

	@FXML
	protected void handleViewCommunityMembers(ActionEvent event) {
		Message message = new Message("#showMembersList", SimpleClient.getCurrentUser());
		try {
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary)Sending message to server: ");

		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}

		// try {
		// // Assume communityId is available and identifies the manager's community
		// String communityId = SimpleClient.getCurrentUser().getCommunity(); // You
		// need to implement this method
		// Message requestMessage = new Message("#getCommunityMembers", communityId);
		// SimpleClient.getClient().sendToServer(requestMessage);
		// } catch (IOException e) {
		// e.printStackTrace();
		// // Optionally, show an alert dialog to the user about the error
		// }
	}

	@FXML
	protected void handleViewHelpRequests(ActionEvent event) {
		Message message = new Message("#showPendingList", SimpleClient.getCurrentUser());
		System.out.println(message);
		try {
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary) Sending req message to server from helpReequest.");
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}
	}

	@FXML
	protected void handleViewCompletedTasks(ActionEvent event) {
		Message message = new Message("#showDoneTasks", SimpleClient.getCurrentUser());
		System.out.println(message);
		try {
			System.out.println(message.getMessage());
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary) Sending req message to server from doneee.");
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}
	}

	public void handlePressingSOS(ActionEvent event) {
		SimpleClient.pressingSOS("primary");
	}

	public void notificationButtonAction(ActionEvent actionEvent) {
		Message message = new Message("#getUserNotifications", SimpleClient.getCurrentUser());
		System.out.println(message);
		try {
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary) Sending req message to server2.");
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}
	}

	@FXML
	protected void handleSOSReports(ActionEvent event) {
		System.out.println("aseelwashere");

		try {
			System.out.println("aseelwashere");
			App.setRoot("SOSReports");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleMyTasksButtonAction(ActionEvent actionEvent) {
		Message message = new Message("#showMyTasks", SimpleClient.getCurrentUser());
		System.out.println(message);
		try {
			System.out.println(message.getMessage());
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary) Sending req showMyTasks message to server from .");
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}
	}
}