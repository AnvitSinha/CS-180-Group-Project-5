import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6969);
            Socket socket = serverSocket.accept();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            ArrayList<String> usernameList = new ArrayList<String>();
            ArrayList<String> passwordList = new ArrayList<String>();

            String currentLine;

            String[] usernameArray;
            String[] passwordArray;

            int verifiedNumber = 0;
            String currentPassword = "";
            String currentUsername = "";
            int arrayIndex = 0;

            while (true) {

                currentLine = reader.readLine();

                if (currentLine.equals("Verify")) {
                    //1: List does not contain username
                    //2: Username and password not correct
                    //3: Credentials valid

                    currentUsername = reader.readLine();
                    currentPassword = reader.readLine();

                    if (!(usernameList.contains(currentUsername))) {
                        writer.println("1");
                    } else {
                        for (int i = 0; i < usernameList.size(); i++) {
                            if (usernameList.get(i).equals(currentUsername)) {
                                arrayIndex = i;
                            }
                        }
                        if (currentPassword.equals(passwordList.get(arrayIndex))) {
                            writer.println("3");
                        } else {
                            writer.println("2");
                        }
                    }

                }
                if (currentLine.equals("UpdateFile")) {
                    currentUsername = reader.readLine();
                    currentPassword = reader.readLine();

                    usernameList.add(currentUsername);
                    passwordList.add(currentPassword);
                }
                if (currentLine.equals("Initialize")) {
                    usernameArray = currentLine.split(",");
                    currentLine = reader.readLine();
                    passwordArray = currentLine.split(",");

                    for (int i = 0; i < usernameArray.length; i++) {
                        usernameList.add(usernameArray[i]);
                    }

                    for (int i = 0; i < passwordArray.length; i++) {
                        passwordList.add(passwordArray[i]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
