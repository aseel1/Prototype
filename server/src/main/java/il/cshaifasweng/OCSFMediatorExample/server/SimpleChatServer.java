package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class SimpleChatServer {

    private static SimpleServer server;

    public static void main(String[] args) throws IOException {
        server = new SimpleServer(3000);
        System.out.println("Server is running");
        server.listen();
    }
}
