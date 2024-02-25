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
	private Button showTasksButton;

	@FXML
	protected void handleShowTasksButtonAction(ActionEvent event) {
		Message message = new Message("#showTasksList");
		try {
			SimpleClient.getClient().sendToServer(message);
			System.out.println("(Primary)Sending message to server: ");

		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			e.printStackTrace();
		}

	}

}
