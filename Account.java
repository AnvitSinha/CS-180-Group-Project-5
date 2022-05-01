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

        allNames = new ArrayList<>();
        allTypes = new ArrayList<>();
        allPasswords = new ArrayList<>();
        allUsernames = new ArrayList<>();

        BufferedReader bfr = new BufferedReader(new FileReader(accountsFile));

        String line;

        while((line = bfr.readLine()) != null) {

            String[] splitWords = line.split(",");

            allNames.add(splitWords[0]);
            allUsernames.add(splitWords[1]);
            allPasswords.add(splitWords[2]);
            allTypes.add(splitWords[3]);

        }


    }   // Initialize from database

    public static void updateDatabaseFile() throws IOException {


        PrintWriter pw = new PrintWriter(new FileWriter(accountsFile, false));

        for (int i = 0; i < allNames.size(); i++) {

            String toWrite = String.format("%s,%s,%s,%s", allNames.get(i), allUsernames.get(i), allPasswords.get(i),
                    allTypes.get(i));

            pw.println(toWrite);
            pw.flush();

        }

        pw.close();

    }   // Update database

    public static boolean isValidCredential(String username, String password) {

        if (!allUsernames.contains(username)) {

            return false;   // false if username is incorrect

        } else {

            return allPasswords.get(allUsernames.indexOf(username)).equals(password);
            // returns true if username and password belong to the same account

        }

    }

    public static boolean isValidUsername(String username) {

        return !allUsernames.contains(username);    // username is valid (true) if it isn't in allUsernames already

    }

    public static boolean addNewAccount(String name, String username, String password, String type) throws IOException {

        if (!isValidUsername(username)) {

            return false;

        }

        allUsernames.add(username);
        allNames.add(name);
        allPasswords.add(password);
        allTypes.add(type);

        PrintWriter pw = new PrintWriter(new FileWriter(accountsFile, true));

        pw.println(String.format("%s,%s,%s,%s", name, username, password, type));

        pw.close();

        return true;

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



    }   // Get user's login info and add account to database

    public Account(String username, String password) {

        this.accountNumber = allUsernames.indexOf(username);
        this.username = allUsernames.get(accountNumber);
        this.password = allPasswords.get(accountNumber);
        this.name = allNames.get(accountNumber);
        this.type = allTypes.get(accountNumber);


    }   // Create account

    public Account(String firstName, String lastName, String username, String password, String type) throws IOException {

        this.username = username;
        allUsernames.add(username);
        this.password = password;
        allPasswords.add(password);
        this.name = firstName + " " + lastName;
        this.type = type;

        Account.updateDatabaseFile();

    }

    public boolean deleteAccount() {

        try {
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

            return true;

        } catch (IOException e) {

            return false;

        }

    }

    public boolean updateUsername(String newUsername) throws IOException {

        if (newUsername.equals(this.username)) {    // Same username as current

            return false;

        } else if (!isValidUsername(newUsername)) { // checks if username is valid

            return false;

        } else {

            this.username = newUsername;
            allUsernames.set(this.accountNumber, newUsername);
            Account.updateDatabaseFile();
            return true;

        }

    }

    public boolean updatePassword(String newPassword) throws IOException {

        if (newPassword.equals(this.password)) {

            return false;

        } else {

            this.password = newPassword;
            allPasswords.set(this.accountNumber, newPassword);
            Account.updateDatabaseFile();
            return true;

        }

    }

    public void updateName(String newName) throws IOException {

        this.name = newName;
        allNames.set(this.accountNumber, newName);
        Account.updateDatabaseFile();

    }

    public String getType() {
        return allTypes.get(this.accountNumber);
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

    @Override
    public String toString() {

        return String.format("Name: %s\nUsername: %s\nAssociated Type: %s\nAccount Number: %s",
        this.name, this.username, this.type, this.accountNumber);
    }

}