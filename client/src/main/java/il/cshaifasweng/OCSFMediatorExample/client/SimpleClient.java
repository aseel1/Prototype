package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import antlr.debug.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	public static Message message;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		message = (Message) msg;
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		} else if (message.getMessage().equals("#showUsersList")) {
			try {
				App.setRoot("secondary"); // calling the fxml function will generate the initliaze of
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (message.getMessage().equals("#showTasksList")) {
			try {
				App.setRoot("tasks"); // calling the fxml function will generate the initliaze of
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#updateTask")) {
			System.out.println("Update request sent to server. Good job!");

		}
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);

		}
		return client;
	}

}
