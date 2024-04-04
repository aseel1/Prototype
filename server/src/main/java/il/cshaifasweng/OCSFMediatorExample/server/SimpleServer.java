package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.DatabaseManager;

import java.io.IOException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

			if (message.startsWith("#warning")) {
				// Perform database operations

			} else if (request.isBlank()) {
				// Perform database operations

			} else if (message.startsWith("#showUsersList")) {

				List<User> users = DatabaseManager.getAllUsers(session);

				message.setObject(users);
				message.setMessage("#showUsersList");
				client.sendToClient(message);

			}else if (message.startsWith("#showMembersList")) {

				User manager = (User) message.getObject();
				String communityManager=manager.getCommunityManager();
				List<User> users = DatabaseManager.getAllUsersByCommunity(session,communityManager);

				message.setObject(users);
				message.setMessage("#showMembersList");
				client.sendToClient(message);

			} else if (message.startsWith("#showTasksList")) {

				List<Task> tasks = DatabaseManager.getAllTasks(session);

				message.setObject(tasks);
				message.setMessage("#showTasksList");

				System.out.println("(SimpleServer)message got from primary and now sending to client");

				try {
					client.sendToClient(message);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("catcedhedh");
				}

				System.out.println("(SimpleServer)message got from primary and now sending to client");

			} else if (message.startsWith("#updateTask")) {

				Task task = (Task) message.getObject();
				System.out.println("taskname" + task.getTaskName() + "taskid" + task.getTaskId()
						+ "taskvolunteer" + task.getVolunteer().getUserName() + "taskstatus" + task.getStatus());

				DatabaseManager.updateTask(session, task);

				message.setMessage("#updateTask");
				message.setObject(task);
				client.sendToClient(message);
			} else if (message.startsWith("#openTask")) {
				message.setMessage("#openTask");
				client.sendToClient(message);
			} else if (message.startsWith("#submitTask")) {
				Task task = (Task) message.getObject(); // derefrence the object from the message
				DatabaseManager.addTask(task, session);

				client.sendToClient(message);
			} else if (message.startsWith("#Login")) {
				User userFromClient = (User) message.getObject(); // User info from the client
				User userFromDB = DatabaseManager.authenticateUser(userFromClient, session);

				if (userFromDB != null && userFromDB.getPassword().equals(userFromClient.getPassword())) {
					if (userFromDB.isLoggedIn()) {
						message.setMessage("User Already Signed In!");
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
			} else if (message.startsWith("#LogOut")) {
				User userFromClient = (User) message.getObject();// Received user info from the client
				User userFromDB = DatabaseManager.authenticateUser(userFromClient,session); // Retrieve the actual user object from DB
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
			}



			else if (message.startsWith("#createUser")) {
				User user = (User) message.getObject();
				System.out.println("User created: " + user.getUserName() + " " + user.getPassword() + " "
						+ user.getAge() + " " + user.getGender() + " " + user.getCommunity() + " " + user.getStatus());
				DatabaseManager.addUser(session, user);
				message.setMessage("#userCreated");
				client.sendToClient(message);
			}
			else if (message.startsWith("#SOSAdd")) {
				SOS newsos = (SOS) message.getObject(); // dereference the object from the message
				DatabaseManager.addSOS(session,newsos);
				String page = message.getMessage().substring("#SOSAdd".length()).trim();
				Message doneMessage = new Message("#addSOSDone",page);
				client.sendToClient(doneMessage);
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
