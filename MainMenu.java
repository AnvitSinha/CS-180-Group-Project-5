import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainMenu {

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 5650)) {

            PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader get = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int mainOption;     // user's choice in main menu
            int teacherOption;  // Teacher's options after logging in
            int teacherAccount; // Teacher's account settings
            int studentOption;  // Student's options after logging in
            int studentAccount;  // Student's account settings

            do {

                String[] mainMenuOptions = { "Log In", "Sign Up", "Exit"};

                mainOption = JOptionPane.showOptionDialog(null, "Main Menu:",
                        "Learning Management System", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, mainMenuOptions, mainMenuOptions[0]);

                switch (mainOption) {

                    case 0 -> {

                        JTextField username = new JTextField();
                        JTextField password = new JPasswordField();
                        Object[] input = {"Username:", username, "Password:", password};

                        int option = JOptionPane.showConfirmDialog(null, input, "Login",
                                JOptionPane.OK_CANCEL_OPTION);

                        if (option == JOptionPane.OK_OPTION) {


                        }

                    }

                    case 1 -> {

                        boolean created = true;

                        do {

                            send.println("newAccount");
                            send.flush();

                            String[] typeOptions = {"Student", "Teacher"};

                            int chosenType = JOptionPane.showOptionDialog(null,"Select Account Type:",
                                    "Learning Management System", JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE, null, typeOptions, typeOptions[0]);

                            String type = "";

                            switch (chosenType) {

                                case 0 -> {type = "student";}

                                case 1 -> {type = "teacher";}

                            }

                            if (type.equals("")) {
                                mainOption = 2;
                                break;
                            }

                            send.println(type);
                            send.flush();

                            JTextField username = new JTextField();
                            JTextField password = new JPasswordField();
                            JTextField name = new JTextField();
                            Object[] input = {"Name:", name, "Username:", username, "Password:", password};

                            int option = JOptionPane.showConfirmDialog(null, input,
                                    "Enter Account Details", JOptionPane.OK_CANCEL_OPTION);

                            if (option == JOptionPane.OK_OPTION) {

                                send.println(name.getText());
                                send.flush();

                                send.println(username.getText());
                                send.flush();

                                send.println(password.getText());
                                send.flush();

                                created = Boolean.parseBoolean(get.readLine());

                                if (created) {

                                    JOptionPane.showMessageDialog(null,
                                            String.format("Account Successfully Created for %s!", name.getText()),
                                            "Learning Management System", JOptionPane.INFORMATION_MESSAGE);


                                } else {

                                    JOptionPane.showMessageDialog(null,
                                            "Username Already Exists!\nPlease Try Again!",
                                            "Learning Management System", JOptionPane.ERROR_MESSAGE);

                                }

                            } else if (option == JOptionPane.CANCEL_OPTION){

                                mainOption = 2;
                                created = true;

                            } else {

                                break;

                            }

                        } while(!created);

                    }

                    case 2 -> {}

                    default -> {

                        mainOption = 2;

                    }

                }

            } while (mainOption != 2);

            JOptionPane.showMessageDialog(null, "Thank You for Using our System!",
                    "Learning Management System", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
