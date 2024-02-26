package il.cshaifasweng.OCSFMediatorExample.server;

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
			} else if (message.startsWith("#showTasksList")) {

				List<Task> tasks = DatabaseManager.getAllTasks(session);

				message.setObject(tasks);
				message.setMessage("#showTasksList");

				System.out.println("(SimpleServer)message got from primary and now sending to client");

				client.sendToClient(message);

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

	private ConnectionToClient getClientById(String string) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getClientById'");
	}
}
