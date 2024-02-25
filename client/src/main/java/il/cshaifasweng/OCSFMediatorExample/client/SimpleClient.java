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
		System.out.println("615sdf" + message.getMessage());
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		} else if (message.getMessage().equals("#showTasksList")) {
			try {
				App.setRoot("secondary");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// EventBus.getDefault().register(this);
			System.out.println("(client)Task came back from server.");

			List<User> users = (List<User>) message.getObject();// users now is a list of users

			for (User user : users) {
				System.out.println("meiw : " + user);
			}

			// FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
			// SecondaryController controller = new SecondaryController();
			// loader.setController(controller);
			// ! controller.setUsers(users);

			Platform.runLater(() -> {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
					Parent root = loader.load();
					SecondaryController secondaryController = loader.getController();

					TableView<User> userTable = secondaryController.getUserTable();

					if (userTable == null) {
						System.out.println("userTable is null");
					} else if (userTable.getItems() == null) {
						System.out.println("userTable.getItems() is null");
					} else {
						System.out.println("userTable and userTable.getItems() are not null");
					}

					ObservableList<User> items = secondaryController.getTableItems();
					if (items != null) {
						System.out.println("Items in userTable: " + items);
					} else {
						System.out.println("No items in userTable");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);

		}
		return client;
	}

}
