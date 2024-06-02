package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import java.util.Map;
import java.util.HashMap;
public class LoggedInClients {
    private static  Map<Integer, ConnectionToClient> loggedInClients = new HashMap<>();

    public static Map<Integer, ConnectionToClient> getLoggedInClients() {
        return loggedInClients;
    }

    public static void setLoggedInClients(Map<Integer, ConnectionToClient> loggedInClients) {
        LoggedInClients.loggedInClients = loggedInClients;
    }

    public static Map<Integer, ConnectionToClient> getClients() {
        return LoggedInClients.loggedInClients;
    }

    // Method to add a client
    public static void addClient(int clientId, ConnectionToClient client) {
        loggedInClients.put(clientId, client);
    }

    // Method to remove a client by ID
    public static void removeClient(int clientId) {
        loggedInClients.remove(clientId);
    }

    // Method to get a client by ID
    public static ConnectionToClient getClientById(int clientId) {
        return loggedInClients.get(clientId);
    }
}
