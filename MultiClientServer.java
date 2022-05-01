import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project 5 - MultiClientServer
 * <p>
 * Handles creating a new thread for each client so that multiple users can use the program at the same time. This
 * accepts users on port 5650. This server runs continuously until it is closed manually and is always ready to accept
 * another user.
 *
 * @author Group 66, L16
 * @version May 2, 2022
 */

public class MultiClientServer {

    public static void main(String[] args) {

        try {

            ServerSocket server = new ServerSocket(5650);   // create server socket at port 5650
            server.setReuseAddress(true);                        // makes the port immediately available upon closing server

            while (true) {  // server continuously waits for users to connect

                Socket client = server.accept();    // create a new socket for each user
                MainServer clientThread = new MainServer(client);   // Create MainServer object for each user

                new Thread(clientThread).start();   // Create a new Thread for each user

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "An Error Occurred At Server!",
                    "Server Error", JOptionPane.WARNING_MESSAGE);

        }

    }

}