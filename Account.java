import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project 4 - Account
 * <p>
 * Class that manages all user accounts by storing them into a database and reading from the database when needed.
 * The class also includes helper methods that help update the database whenever the use wants to change their
 * credentials, or a new account is added.
 *
 * @author Group 66, L16
 * @version April 11, 2022
 */

public class Account {

    private static ArrayList<String> allUsernames = new ArrayList<>();
    private static ArrayList<String> allPasswords = new ArrayList<>();
    private static ArrayList<String> allTypes = new ArrayList<>();
    private static ArrayList<String> allNames = new ArrayList<>();
    private static final String accountsFile = "accounts.txt";

    private String username;
    private String password;
    public String name;
    private String type;
    private int accountNumber;

    public static void initializeAccounts() throws IOException {

        BufferedReader bfr = new BufferedReader(new FileReader(accountsFile));

        String line;

        while((line = bfr.readLine()) != null) {

            String[] splitWords = line.split(",");

            allNames.add(splitWords[0]);
            allUsernames.add(splitWords[1]);
            allPasswords.add(splitWords[2]);
            allTypes.add(splitWords[3]);

        }


    }

    private static void updateDatabaseFile() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(accountsFile, false))) {

            for (int i = 0; i < allNames.size(); i++) {

                String toWrite = String.format("%s,%s,%s,%s", allNames.get(i), allUsernames.get(i), allPasswords.get(i),
                        allTypes.get(i));

                pw.println(toWrite);
            }

        } catch (IOException e) {
            System.out.println("An Error Occurred!");
        }

    }

    public static boolean isValidCredential(String username, String password) {

        if (!allUsernames.contains(username)) {

            return false;   // false if username is incorrect

        } else {

            return allPasswords.get(allUsernames.indexOf(username)).equals(password);
            // returns true if username and password belong to the same account

        }

    }

    public Account(Scanner scanner, String type) {

        System.out.println("Enter User's Full Name: ");
        String fullName = scanner.nextLine();

        String username;

        do {

            System.out.println("Enter a Username:");
            username = scanner.nextLine();

            if(!isValidUsername(username)) {
                System.out.println("Username is already taken!");
            }   // Invalid username message

        } while(!isValidUsername(username));  // Username not valid check

        System.out.println("Set Password: ");
        String password = scanner.nextLine();

        this.username = username;
        allUsernames.add(username);
        this.password = password;
        allPasswords.add(password);
        this.name = fullName;
        allNames.add(fullName);
        this.type = type;
        allTypes.add(type);
        this.accountNumber = allUsernames.indexOf(username);

        try (PrintWriter pw = new PrintWriter(new FileWriter(accountsFile, true))) {

            pw.println(this.toString());

        } catch (IOException e) {
            System.out.println("An Error Occurred!");
        }

        if (type.equals("student")) {

            GradeBook.addNewStudent(this.name);

        }


    }   // Get user's login info and add account to database

    public Account(String username, String password) {

        this.accountNumber = allUsernames.indexOf(username);
        this.username = allUsernames.get(accountNumber);
        this.password = allPasswords.get(accountNumber);
        this.name = allNames.get(accountNumber);


    }

    public Account(String firstName, String lastName, String username, String password, String type) {

        this.username = username;
        allUsernames.add(username);
        this.password = password;
        allPasswords.add(password);
        this.name = firstName + " " + lastName;
        this.type = type;

        try (PrintWriter pw = new PrintWriter(new FileWriter(accountsFile, true))) {

            pw.println(this.toString());

        } catch (IOException e) {
            System.out.println("An Error Occurred!");;
        }

    }

    public void deleteAccount() {

        // remove account from database arraylists
        allNames.remove(this.accountNumber);
        allPasswords.remove(this.accountNumber);
        allTypes.remove(this.accountNumber);
        allUsernames.remove(this.accountNumber);

        // set all fields to blank
        this.username = "";
        this.password = "";
        this.name = "";
        this.type = "";
        this.accountNumber = -1;    // set account number to -1 (invalid index)

        // Update the database
        Account.updateDatabaseFile();
        System.out.println("Successfully Deleted");

    }

    public void updateAccount(Scanner scanner, int choice) {

        switch (choice) {

            case 1 -> {

                String username;
                boolean valid = true;   // initialize boolean to check if valid new username is given

                do {

                    System.out.println("Enter a Username:");
                    username = scanner.nextLine();

                    if (username.equals(this.username)) {       // if current username is entered

                        System.out.println("New Username cannot be same as older Username!");
                        valid = false;

                    } else if(!isValidUsername(username)) {     // if username is valid
                        System.out.println("Username is already taken!");
                        valid = false;
                    }   // Invalid username message

                } while(!valid);  // Username not valid check

                this.username = username;
                allUsernames.set(this.accountNumber, username);

            }       // Update username

            case 2 -> {

                String password;

                do {

                    System.out.println("Enter a Password:");
                    password = scanner.nextLine();

                    if (password.equals(this.password)) {   // checks if same password entered

                        System.out.println("New Password cannot be same as older Password!");

                    }

                } while(password.equals(this.password));  // Username not valid check

                this.password = password;
                allPasswords.set(this.accountNumber, password);

            }       // update password

            case 3 -> {

                System.out.println("Enter new Name:");
                String newName = scanner.nextLine();

                this.name = newName;        // sets new name
                allNames.set(this.accountNumber, newName);

            }       // update name

            default -> {
                System.out.println("Invalid Choice!");
            }       // invalid choice

        }

        System.out.println("Credentials Changed!");

        Account.updateDatabaseFile();   // update database file

    }

    public String getType() {
        return allTypes.get(this.accountNumber);
    }

    public boolean isValidUsername(String username) {

        return !allUsernames.contains(username);    // username is valid (true) if it isn't in allUsernames already

    }

    public String getFullName() {
        return this.name;
    }

    public int getAccountSeed() {

        int first = this.password.length();         // length of password
        int second = this.username.length();        // length of username
        int third = this.name.length();             // length of name
        int fourth = this.password.charAt(first/2); // integer representation of middle character of password
        int fifth = this.username.charAt(second/2); // integer representation of middle character of username

        return (first + second + third + fourth + fifth);

    }   // creates a random seed for an account based on username and password of the user

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        allUsernames.set(allUsernames.indexOf(this.username), username);
        this.username = username;
    }

    public void setPassword(String password) {
        allPasswords.set(allPasswords.indexOf(this.password), password);
        this.password = password;
    }

    public void setFullName(String fullName) {
        allNames.set(allNames.indexOf(this.name), fullName);
        this.name = fullName;
    }

    @Override
    public String toString() {
        System.out.println(this.type);
        return String.format("%s,%s,%s,%s", this.name, this.username, this.password, this.type);
    }

}