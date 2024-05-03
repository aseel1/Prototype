package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import javafx.scene.image.Image;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

import static il.cshaifasweng.OCSFMediatorExample.server.ocsf.DatabaseManager.getUserById;
import static il.cshaifasweng.OCSFMediatorExample.server.ocsf.DatabaseManager.addTask;
import static il.cshaifasweng.OCSFMediatorExample.server.ocsf.DatabaseManager.updateNotification;


public class SimpleServer extends AbstractServer {

	public SimpleServer(int port) {
		super(port);
		DatabaseManager.initialize(); // this is for the initialization of the database manager

	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

		System.out.println("Received a message from: " + client.toString());

		Message message = (Message) msg;
		String request = message.getMessage();

		Session session = DatabaseManager.getSessionFactory().openSession();
		System.out.println("opened session");
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			if (request.startsWith("#warning")) {
				// Perform database operations

			} else if (request.isBlank()) {
				// Perform database operations

			} else if (request.startsWith("#showUsersList")) {

				List<User> users = DatabaseManager.getAllUsers(session);

				message.setObject(users);
				message.setMessage("#showUsersList");
				client.sendToClient(message);

			} else if (request.startsWith("#showMembersList")) {

				User manager = (User) message.getObject();
				String communityManager = manager.getCommunityManager();
				List<User> users = DatabaseManager.getAllUsersByCommunity(session, communityManager);

				message.setObject(users);
				message.setMessage("#showMembersList");
				client.sendToClient(message);

			} else if (request.startsWith("#updatePic")) {

			User user = (User) message.getObject();
			File selectedFile= (File)message.getSecondObject();
			user.setImageFile(selectedFile);
			DatabaseManager.updateUser(session, user);
			message.setObject(user);
			System.out.println("(SimpleServer)message got from primary and now updating pic");
				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}

		} else if (request.startsWith("#showTasksList")) {
				User thisUser = (User) message.getObject();
				List<Task> tasks = DatabaseManager.getTasksByStatusAndUser(session, thisUser);
				User userfromDB= DatabaseManager.getUserById(thisUser.getId(),session);
				userfromDB.setTaskListOpen(true);
				message.setObject(tasks);
				message.setMessage("#showTasksList");
				System.out.println("(SimpleServer)message got from primary and now sending to client");

				client.sendToClient(message);
				//sendToAllClients("#refreshTable");
					//.broadcast(message);


			} else if (request.startsWith("#showPendingList")) {
				// This assumes the message object contains the User or enough information to
				// fetch the User
				User userFromClient = (User) message.getObject(); // Make sure this casting is valid based on your
				// message structure
				String community = userFromClient.getCommunityManager(); // Adjust according to how you access the community in
				// your User entity
				List<Task> tasks = DatabaseManager.getTasksByStatusAndCommunity(session, "pending", community);
				System.out.println(tasks);
				message.setObject(tasks);
				message.setMessage("#showPendingList");
				System.out.println(message.getMessage());

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if (request.startsWith("#refreshMyTable")) {

				User thisUser = (User) message.getObject();
				User userFromDB = DatabaseManager.getUserById(thisUser.getId(),session);
				if(userFromDB.isTaskListOpen()) {
					List<Task> tasks = DatabaseManager.getTasksByStatusAndUser(session, thisUser);
					message.setObject(tasks);
					message.setMessage("#showTasksList");
					client.sendToClient(message);
					System.out.println("(SimpleServer)message got from primary and now sending to client");

					//sendToAllClients(message);
				}
				//.broadcast(message);


			}else if (request.startsWith("#closeTaskList")) {
                User userFromClient=(User) message.getObject();
				User userFromDB=getUserById(userFromClient.getId(),session);
				userFromDB.setTaskListOpen(false);

			} else if (request.startsWith("#showDoneTasks")) {
				// This assumes the message object contains the User or enough information to
				// fetch the User
				User userFromClient = (User) message.getObject(); // Make sure this casting is valid based on your
																	// message structure
				String community = userFromClient.getCommunityManager();// Adjust according to how you access the
																		// community in your User entity
				List<Task> tasks = DatabaseManager.getTasksDone(session, "done", community);
				System.out.println(tasks);
				message.setObject(tasks);
				message.setMessage("#showDoneList");
				System.out.println(message.getMessage());
				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (request.startsWith("#showMyTasks")) {
				// This assumes the message object contains the User or enough information to
				// fetch the User
				User userFromClient = (User) message.getObject(); // Make sure this casting is valid based on your
				// message structure
				// community in your User entity
				List<Task> tasks = DatabaseManager.getTasksByVolunteer(session,userFromClient);
				System.out.println(tasks);
				message.setObject(tasks);
				message.setMessage("#showMyTasksList");
				System.out.println(message.getMessage());

				try {
					client.sendToClient(message);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (request.startsWith("#showSOS")) {
				// Split the message to extract parameters
				String[] parts = message.getObject().toString().split(" ", 3);
				System.out.println(parts[0]);
				System.out.println(parts[1]);
				System.out.println(parts[2]);
				String startDate = parts[0];
				String endDate = parts[1];
				String communityName = parts[2];

				List<SOS> sosList;
				try {
					// Determine if we're fetching for a specific community or all communities
					if (communityName.equalsIgnoreCase("all")) {
						// Fetch SOS records across all communities within the date range
						sosList = DatabaseManager.getSOSBetweenDates(session, startDate, endDate);
						System.out.println(startDate + endDate);
					} else {
						// Fetch SOS records for a specific community within the date range
						sosList = DatabaseManager.getSOSByCommunityAndDates(session, communityName, startDate, endDate);
					}
					// Prepare the response with the fetched SOS records
					message.setObject(sosList); // Set the list of SOS records as the message object
					message.setMessage("#showSOSResponse"); // Indicate this message is a response to the #showSOS
															// request
					client.sendToClient(message);
				} catch (IOException e) {
					System.err.println("Failed to send SOS list to client: " + e.getMessage());
				} catch (HibernateException e) {
					System.err.println("Database access earrrror: " + e.getMessage());
				}
			} else if (request.startsWith("#updateTask")) {

				Task task = (Task) message.getObject();
				System.out.println("taskname" + task.getTaskName() + "taskid" + task.getTaskId()
						+ "taskvolunteer" + task.getVolunteer().getUserName() + "taskstatus" + task.getStatus()
						+ "taskuser" + task.getUser().getUserName() + "taskdetails" + task.getDetails());

				DatabaseManager.updateTask(session, task);

				message.setMessage("#updateTask");
				message.setObject(task);
				client.sendToClient(message);
			} else if (request.startsWith("#openTask")) {
				message.setMessage("#openTask");
				client.sendToClient(message);
			}

			// when creating a new task. add it and send a message back to client to get the
			// user and then find out whe is the manager
			else if (request.startsWith("#submitTask")) {
				// receive task, print it and update in taskstable
				Task task = (Task) message.getObject(); // derefrence the object from the message
				System.out.println(" taskname " + task.getTaskName() + " taskid " + task.getTaskId()
						+ " taskstatus " + task.getStatus() + " taskdetails " + task.getDetails());
				DatabaseManager.addTask(task, session);
				System.out.println("task updated as pending");
				// now we need the user in order to know the manager
				User currentUser = task.getUser();
				List<User> users = DatabaseManager.getAllUsers(session);
				int foundManager = 0;
				for (User user : users) {
					if (Objects.equals(currentUser.getCommunity(), user.getCommunityManager())) {
						// if reached here, then found manager :))))
						// update task managerID:
						System.out.println("found manager, updating task manager ID and sending him a notification");
						foundManager = 1;
						task.setManagerId(user.getId());
						message.setSecondObject(user);
						break;
					}
				}
				if (foundManager == 0) {
					System.err.println("Manager not found");
					task.setStatus("manager not found");
					message.setSecondObject(null);
				}
				message.setMessage("#taskSubmitted");
				client.sendToClient(message);
			}

			// manager approved the task. just update the status
			else if (request.startsWith("#managerApproved")) {
				System.out.println("manager approved of task");
				// manager approves of task, add it to list
				Task task = (Task) message.getObject(); // derefrence the object from the message
				task.setStatus("idle");
				DatabaseManager.updateTask(session, task);
				System.out.println("task status updated to idle \n" + task.getTaskId());
				message = new Message("#managerApproved",task);
				client.sendToClient(message);
				message= new Message("#refreshTable");
				sendToAllClients(message);
			}

			// manager declined the task.
			else if (request.startsWith("#managerDeclined")) {
				System.out.println("manager declined task" + message.getMessage());

				Task task = (Task) message.getObject(); // derefrence the object from the message
				task.setStatus("declined");
				DatabaseManager.updateTask(session, task);
				System.out.println("task status updated to 'declined'");
				// we need to send a notification!
				client.sendToClient(message);
			}

			else if (request.startsWith("#cancelDecline")) {
				System.out.println("manager canceled decline");
				Task task = (Task) message.getObject(); // derefrence the object from the message
				task.setStatus("pending");
				DatabaseManager.updateTask(session, task);
				System.out.println("task status updated back to 'pending'");
				// we need to send a notification!
				client.sendToClient(message);
			}

			else if (request.startsWith("#Login")) {
			//	User userFromClient = (User) message.getObject(); // User info from the client
				String password = (String) message.getObject();
				String userName = message.getMessage().substring("#Login".length());
				List<User> usersFromDB = DatabaseManager.authenticateUser(userName, session);
				User userFromDB=null;
				for(User user: usersFromDB){
					if(user.verifyPassword(password))
						userFromDB=user;
				}

				if (userFromDB != null ) {
					if (userFromDB.isLoggedIn()) {
						message.setMessage("#User Already Signed In!");
					} else {
						userFromDB.setLoggedIn(true); // Set the user as logged in
						session.update(userFromDB); // Make sure to update the user in the database
						System.err.println("Login success");
						message.setMessage("#loginSuccess");
						message.setObject(userFromDB); // Return the updated user object
					}
				} else {
					System.err.println("Login failed");
					message.setMessage("#loginFailed");
				}
				client.sendToClient(message);
			} else if (request.startsWith("#LogOut")) {
				User userFromClient = (User) message.getObject();// Received user info from the client
				User userFromDB = DatabaseManager.getUserById(userFromClient.getId(), session); // Retrieve the actual user
																								// object from DB
				if (userFromDB != null && userFromDB.isLoggedIn()) {
					userFromDB.setLoggedIn(false); // Correctly update the userFromDB instance
					session.update(userFromDB); // Persist the changes for userFromDB
					// Prepare a response message indicating success
					Message responseMessage = new Message("#LoggedOut");
					client.sendToClient(responseMessage); // Send success message back to client
					System.out.println("User logged out successfully: " + userFromDB.getUserName());
				} else {
					// Prepare a response message indicating failure or not logged in
					Message responseMessage = new Message("#logoutFailed");
					client.sendToClient(responseMessage); // Send failure message back to client
					System.err.println("Logout failed or user was not logged in.");
				}
				tx.commit(); // This line commits the transaction including the loggedIn status update
			} else if (request.startsWith("changeStatusToIP")) {
				Task thisTask = (Task) message.getObject();
				System.out.println(thisTask.getTaskId());
				Task task=DatabaseManager.authenticateTask(thisTask.getTaskId(),session);
				System.out.println(task.getStatus());
				User taskVolunteer = (User) message.getSecondObject();
				if (task.getStatus().equals("idle")) {
					System.out.println("innnnnnn");
					task.setStatus("in Process");
					LocalDateTime now = LocalDateTime.now().withNano(0);
//					thisTask.setVolTime(now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond());
//					String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					task.setVolDate(now);
					task.setVolunteer(taskVolunteer);
					// DatabaseManager.updateTask(session, thisTask);
					message.setObject("Done");
				} else {
					message.setObject("Failed");
				}
				client.sendToClient(message);
				message = new Message("#refreshTable");
				sendToAllClients(message);
			} else if (request.startsWith("changeStatusToDone")) {
				Task thisTask = (Task) message.getObject();
				if (thisTask.getStatus().equals("in Process")) {
					thisTask.setStatus("done");
					DatabaseManager.updateTask(session, thisTask);
					message.setMessage("TheStatusChanged");
					client.sendToClient(message);
				}


			} else if (request.startsWith("#createUser")) {
				User user = (User) message.getObject();
				System.out.println("User created: " + user.getUserName() + " " + user.getPasswordHash() + " "
						+ user.getAge() + " " + user.getGender() + " " + user.getCommunity() + " " + user.getStatus());
				DatabaseManager.addUser(session, user);
				message.setMessage("#userCreated");
				client.sendToClient(message);
			} else if (request.startsWith("#SOSAdd")) {
				SOS newsos = (SOS) message.getObject(); // dereference the object from the message
				DatabaseManager.addSOS(session, newsos);
				String page = message.getMessage().substring("#SOSAdd".length()).trim();
				Message doneMessage = new Message("#addSOSDone", page);
				client.sendToClient(doneMessage);
			} else if (request.startsWith("#getUserNotifications")) {

				User user = (User) message.getObject();
				List<Notification> notifications = DatabaseManager.getUsersNotifications(session, user);

				message.setObject(notifications);
				message.setMessage("#showNotificationsList");
				client.sendToClient(message);

			} else if (message.getMessage().equals("#addNotification")) {
				List<User> users = DatabaseManager.getAllUsers(session);
				int receiverId = (int) message.getObject();
				User receiver = null;
				for (User user : users) {
					if ((user != null) && user.getId() == receiverId) {
						receiver = user;
					}
				}
				Notification sendNot = (Notification) message.getSecondObject();
				sendNot.setRecipient(receiver);
				session.save(sendNot);
				updateNotification(session, sendNot);
				session.clear();
			}

			tx.commit();

		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e; // Or display error message
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the session
			if (session != null)
				session.close();
			System.out.println("closed session");
		}
	}

}