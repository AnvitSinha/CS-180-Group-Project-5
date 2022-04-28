import java.net.*;

public class MultiClientServer {

    public static void main(String[] args) {

        try {

            ServerSocket server = new ServerSocket(5650);
            server.setReuseAddress(true);

            while (true) {

                Socket client = server.accept();
                MainServer clientThread = new MainServer(client);

                new Thread(clientThread).start();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}