package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.scene.control.ButtonType;
import org.greenrobot.eventbus.EventBus;

import antlr.debug.MessageEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	public static Message message;

	private static User currentUser = null; // this is for the current user(logged in user) holds his details

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

		}else if (message.getMessage().equals("#showMembersList")) {
			try {
				App.setRoot("MembersList"); // calling the fxml function will generate the initliaze of
			} catch (IOException e) {
				e.printStackTrace();
			}

		}else if (message.getMessage().equals("#showTasksList")) {
			try {
				System.out.println("(Client) Tasks list received from server.");
				App.setRoot("Tasks"); // calling the fxml function will generate the initliaze of

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#updateTask")) {
			System.out.println("Update request sent to server. Good job!");

		} else if (message.getMessage().equals("#openTask")) {
			try {
				App.setRoot("TaskForm");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#loginSuccess")) {
			try {
				currentUser = (User) message.getObject();
				System.err.println("Login success. Welcome, " + currentUser.getUserName() + " "
						+ currentUser.getPassword() + " " + currentUser.getAge() + " " + currentUser.getGender() + " "
						+ currentUser.getCommunity()+ currentUser.getCommunityManager());
				App.setRoot("primary");
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						PrimaryController.getInstance().updateLabels(currentUser.getUserName(),
								currentUser.getStatus());
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (message.getMessage().equals("#loginFailed")) {
			try {
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Login Failed");
					alert.setHeaderText("null");
					alert.setContentText("Login failed. Please try again.");

					alert.showAndWait();
				});
			} catch (Exception e) {
			}
		}else if (message.getMessage().equals("#LoggedOut")) {
			Platform.runLater(() -> {
				try {
					App.setRoot("Login"); // Navigate back to the login screen
					showAlert("Logout Successful", "You have been successfully logged out.", Alert.AlertType.INFORMATION);
				} catch (IOException e) {
					e.printStackTrace();
					showAlert("Error", "Failed to load the login page.", Alert.AlertType.ERROR);
				}
			});
		}

		else if (message.getMessage().equals("#userCreated")) {
			try {
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("User Created");
					alert.setHeaderText("Done");
					alert.setContentText("User created successfully.");

					alert.showAndWait();
					try {
						App.setRoot("Login");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
			}
		} else if (message.getMessage().equals("#submitTask")) {
			try {
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Task Submitted");
					alert.setHeaderText("Done");
					alert.setContentText("Task submitted successfully.");

					alert.showAndWait();
					try {
						App.setRoot("primary");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
			}
		}
		else if (message.getMessage().equals("#addSOSDone")) {
			Platform.runLater(() -> {
				try {
					String currentFXMLPage = (String) message.getObject(); // Implement this method to get the current FXML page
					App.setRoot(currentFXMLPage); // Navigate back to the current page
					showAlert("your request have received", "Help on the way!", Alert.AlertType.INFORMATION);
				} catch (IOException e) {
					e.printStackTrace();
					showAlert("Error", "Failed to contact help.", Alert.AlertType.ERROR);
				}
			});
		}


	}

	public static User getCurrentUser() { // retreive the current user
		return currentUser;
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);

		}
		return client;
	}

	private void showAlert(String title, String content, Alert.AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	//here we use it to when we press on the SOS button
	protected static void pressingSOS(String page){
		SOS newSOS=new SOS();
		if(!page.equals("Login")&&!page.equals("UserCreationForm")) {
			newSOS.setUser(getCurrentUser());
        }
		String sendingMassage ="#SOSAdd" + page;
		Message message = new Message(sendingMassage, newSOS);
		try {
			getClient().sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

