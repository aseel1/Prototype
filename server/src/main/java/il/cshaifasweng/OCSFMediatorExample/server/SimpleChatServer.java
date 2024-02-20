package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class SimpleChatServer {

    private static SimpleServer server;

    public static void main(String[] args) throws IOException {
        server = new SimpleServer(3000); // create server and initialize it(crates the database and tables, and
                                         // populates them with data)
        System.out.println("Server is running");
        server.listen();
    }
}
