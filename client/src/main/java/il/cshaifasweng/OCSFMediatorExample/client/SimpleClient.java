package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

//for custom alert:
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	public static Message message;
	public static Message tableMessage;

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
			tableMessage = message;
			try {
				App.setRoot("secondary"); // calling the fxml function will generate the initliaze of
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (message.getMessage().equals("#showMembersList")) {
			tableMessage = message;
			try {
				App.setRoot("secondary"); // calling the fxml function will generate the initliaze of
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (message.getMessage().equals("#showTasksList")) {
			tableMessage = message;
			try {
				System.out.println("(Client) Tasks list received from server");
				tableMessage = message;
				App.setRoot("Tasks"); // calling the fxml function will generate the initliaze of

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#showPendingList")) {
			try {
				System.out.println("(Client) Tasks list received from server pendingg");
				tableMessage = message;
				App.setRoot("Tasks"); // calling the fxml function will generate the initliaze of
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#showDoneList")) {
			try {
				System.out.println("(Client) Tasks list received from server doneee");
				tableMessage = message;
				App.setRoot("Tasks"); // calling the fxml function will generate the initliaze of
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#showMyTasksList")) {
			try {
				System.out.println("(Client) User Tasks list received from server ");
				tableMessage = message;
				App.setRoot("Tasks"); // calling the fxml function will generate the initliaze of
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#updateTask")) {
			System.out.println("Update request sent to server. Good job!");

		} else if (message.getMessage().equals("changeStatusToIP")) {
			if ("Done".equals(message.getObject())) {
				Platform.runLater(() -> {
					try {
						App.setRoot("Tasks"); // Navigate back to the current page
						showAlert("Successful", "You can now proceed by start doing the task," +
								" when the task is done please change the status.", Alert.AlertType.INFORMATION);
					} catch (IOException e) {
						e.printStackTrace();
					}

				});
			} else {
				Platform.runLater(() -> {
					try {
						App.setRoot("Tasks"); // Navigate back to the current page
						showAlert("Error", "Cannot choose this task please choose another.", Alert.AlertType.ERROR);
					} catch (IOException e) {
						e.printStackTrace();
					}

				});
			}
			// to update the table again
			// Message message = new Message("#refreshTable",
			// SimpleClient.getCurrentUser());
			// try {
			// SimpleClient.getClient().sendToServer(message);
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		} else if (message.getMessage().equals("#refreshTable")) {
			// to update the table again
			Message message = new Message("#refreshMyTable", SimpleClient.getCurrentUser());
			try {
				SimpleClient.getClient().sendToServer(message);
				System.out.println("(client)refreshMytable");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("TheStatusChanged")) {
			// to update the table again
			Message message = new Message("#showMyTasksList", SimpleClient.getCurrentUser());
			try {
				SimpleClient.getClient().sendToServer(message);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#taskSubmitted")) {
			Platform.runLater(() -> {
				showAlert("Task was submitted", "Now we're waiting for your manager's approval!",
						Alert.AlertType.INFORMATION);
				Task task = (Task) message.getObject();
				User manager = (User) message.getSecondObject();
				if (manager != null) {
					// sending a notification to manager
					String notification = ("You have a new task request: " + " taskname= " + task.getTaskName()
							+ " taskid= " + task.getTaskId() +
							" taskstatus= " + task.getStatus() + " taskdetails= " + task.getDetails());
					SimpleClient.sendNotification(SimpleClient.currentUser, manager.getId(), notification);
					//sending a new task event via eventBus:
					try {
						TaskSubmittedEvent event = new TaskSubmittedEvent(task);
						EventBusManager.getEventBus().post(event);
						System.out.println("(simple client) event sent from taskSubmitted func");
					} catch (Exception e) {
						System.err.println("Error sending event: " + e.getMessage());
						e.printStackTrace();
					}
				}
			});
		} else if (message.getMessage().equals("#managerApproved")) {
			Task task = (Task) message.getObject();
			Platform.runLater(() -> {
				showAlert("Approved!", "The request has been approved :)", Alert.AlertType.INFORMATION);

				// sending a notification to everyone
				String txt = "A new help-request was opened! Come on, help us help them! TaskId=" + task.getTaskId();
				SimpleClient.sendNotification(task.getUser(), -1, txt);
				Message message = new Message("#showPendingList", SimpleClient.getCurrentUser());
				try {
					SimpleClient.getClient().sendToServer(message);
					System.out.println("(Primary) Sending req message to server from helpReequest.");
				} catch (IOException e) {
					System.out.println("Failed to connect to the server.");
					e.printStackTrace();

				}
			});
		}

		else if (message.getMessage().equals("#managerDeclined")) {
			Platform.runLater(() -> {
				System.out.println("(Simple Client) manager declined");
				Message savedMessage = message;
				// Create a TextInputDialog instead of CustomAlert
				Message message = new Message("#showPendingList", SimpleClient.getCurrentUser());
				try {
					SimpleClient.getClient().sendToServer(message);
					System.out.println("(Primary) Sending req message to server from helpReequest.");
				} catch (IOException e) {
					System.out.println("Failed to connect to the server.");
					e.printStackTrace();
				}
				TextInputDialog textInputDialog = new TextInputDialog();
				textInputDialog.setTitle("Explanation");
				textInputDialog
						.setHeaderText("Please fill in an explanation to send to the user who opened this task.");

				// Show the dialog and wait for user input
				Optional<String> result = textInputDialog.showAndWait();

				// Check if user input is present
				if (result.isPresent()) {
					// Process user input if available
					String enteredText = result.get();
					System.out.println(savedMessage.getMessage() + savedMessage.getObject());
					Task task = (Task) savedMessage.getObject();
					SimpleClient.sendNotification(SimpleClient.currentUser, task.getUser().getId(), enteredText);
					System.out.println("(Simple Client) Sent a notification to user: " + enteredText);
				} else {
					// Handle cancel action or dialog closure
					System.out.println("(Simple Client) User canceled or closed the dialog");
					savedMessage.setMessage("#cancelDecline");
					try {
						getClient().sendToServer(savedMessage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

		}
		else if (message.getMessage().equals("#boxNotChecked")) {
			Platform.runLater(() -> {
				showAlert("Declaration and Task Selection Required", "Please ensure that you have checked the declaration and selected a task before proceeding.)", Alert.AlertType.INFORMATION);
			});
		}

		else if (message.getMessage().equals("#openTask")) {
			try {
				App.setRoot("TaskForm");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (message.getMessage().equals("#showSOSResponse")) {
			System.out.println(message.getObject());
			SOSReportsController.updateHistogramFromMessage(message);
		} else if (message.getMessage().equals("#loginSuccess")) {
			try {
				currentUser = (User) message.getObject();
				System.err.println("Login success. Welcome, " + currentUser.getUserName() + " "
						+ currentUser.getPasswordHash() + " " + currentUser.getAge() + " " + currentUser.getGender()
						+ " "
						+ currentUser.getCommunity() + currentUser.getCommunityManager());
				App.setRoot("primary");

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (message.getMessage().equals("#updatePic")) {
			try {
				currentUser = (User) message.getObject();
				System.err.println("pic updated successfully");
				App.setRoot("primary");

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (message.getMessage().equals("#loginFailed")) {
			try {
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Login Failed");
					alert.setHeaderText("The username and password you entered are incorrect.");
					alert.setContentText("Please check your credentials and try again.");

					alert.showAndWait();
				});
			} catch (Exception e) {
			}
		} else if (message.getMessage().equals("#User Already Signed In!")) {
			try {
				Platform.runLater(() -> {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Login Failed");
					alert.setHeaderText("User Already Signed In!");
					alert.showAndWait();
				});
			} catch (Exception e) {
			}
		} else if (message.getMessage().equals("#LoggedOut")) {
			Platform.runLater(() -> {
				try {
					App.setRoot("Login"); // Navigate back to the login screen
					showAlert("Logout Successful", "You have been successfully logged out.",
							Alert.AlertType.INFORMATION);
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
		}else if (message.getMessage().equals("#refreshRequestTable")) {
			//if reached here, then manager is online, refresh his request tasks table :)
			TaskSubmittedEvent event = new TaskSubmittedEvent((Task)message.getObject());
			EventBusManager.getEventBus().post(event);
			System.out.println("(Simple Client) event sent by "+getCurrentUser().getUserName());

		} else if (message.getMessage().equals("#addSOSDone")) {
			Platform.runLater(() -> {
				try {
					String currentFXMLPage = (String) message.getObject(); // Implement this method to get the current
																			// FXML page
					App.setRoot(currentFXMLPage); // Navigate back to the current page
					if(currentFXMLPage.equals("Login"))
						showAlert("your request have received", "Help on the way!.\n Please sign in or create an account!", Alert.AlertType.INFORMATION);
					else
						showAlert("your request have received", "Help on the way!", Alert.AlertType.INFORMATION);
				} catch (IOException e) {
					e.printStackTrace();
					showAlert("Error", "Failed to contact help.", Alert.AlertType.ERROR);
				}
			});
		} else if (message.getMessage().equals("#notificationSent")) {
			//sending new notification event via eventBus:
			try {
				Notification newNotification= (Notification) message.getObject();
				//System.out.println("(simple client) in notificationSent func, recipient is:"+ newNotification.getRecipient().getUserName());
				NotificationReceivedEvent event = new NotificationReceivedEvent(newNotification);
				EventBusManager.getEventBus().post(event);
				System.out.println("(simple client) event sent from notificationSent func");
			} catch (Exception e) {
				System.err.println("Error sending event: " + e.getMessage());
				e.printStackTrace();
			}

	}else if (message.getMessage().equals("#showNotificationsList")) {
			System.out.println(message.getMessage() + "haayyhee");
			tableMessage = message;
			try {
				System.out.println("(Client) Notification list received from server.");
				App.setRoot("Notifications"); // calling the fxml function will generate the initliaze of

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	// example how to use it
	// receiver_id = -1 if its for all
	// SimpleClient.sendNotification(SimpleClient.getCurrentUser(),receiver_id,"message");
	public static void sendNotification(User sender, int receiverId, String notification) {
		Notification sendNot = new Notification(sender, null, notification);
		Message message = new Message("#addNotification", receiverId, sendNot);
		try {
			SimpleClient.getClient().sendToServer(message);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static User getCurrentUser() { // retrieve the current user
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

	// here we use it to when we press on the SOS button
	protected static void pressingSOS(String page) {
		SOS newSOS = new SOS();
		if (!page.equals("Login") && !page.equals("UserCreationForm")) {
			newSOS.setUser(getCurrentUser());
		}
		String sendingMassage = "#SOSAdd" + page;
		Message message = new Message(sendingMassage, newSOS);
		try {
			getClient().sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
