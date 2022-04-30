import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenu {

    private static String quizFormatter(String input) {

        ArrayList<String> container = new ArrayList<String>(Arrays.asList(input.split("&")));


        StringBuilder sb = new StringBuilder();

        for (String s : container) {

            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    //Formats specific server output
    private static String answersFormatter(ArrayList<String> input) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.size() - 1; i++) {

            sb.append(input.get(i));
            sb.append("&");
        }
        sb.append(input.get(input.size() - 1));

        return sb.toString();
    }

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 5650)) {

            PrintWriter send = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader get = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int mainOption;     // user's choice in main menu
            int systemOption;   // User's options after logging in
            int userAccount;    // user's account settings

            do {

                Account.initializeAccounts();
                Course.initializeCourses();
                GradeBook.initializeStudentGradebook();
                QuizFile q = new QuizFile("quiz_tester.txt");

                String[] mainMenuOptions = {"Log In", "Sign Up", "Exit"};

                mainOption = JOptionPane.showOptionDialog(null, "Main Menu:",
                        "Learning Management System", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, mainMenuOptions, mainMenuOptions[0]);

                switch (mainOption) {

                    case 0 -> {

                        boolean verified;
                        String type = null;
                        String name = null;
                        Account thisAccount = null;

                        do {

                            send.println("verify");

                            JTextField username = new JTextField();
                            JTextField password = new JPasswordField();
                            Object[] input = {"Username:", username, "Password:", password};

                            int option = JOptionPane.showConfirmDialog(null, input, "Login",
                                    JOptionPane.OK_CANCEL_OPTION);

                            if (option == JOptionPane.OK_OPTION) {

                                send.println(username.getText());
                                send.flush();

                                send.println(password.getText());
                                send.flush();

                                verified = Boolean.parseBoolean(get.readLine());

                                if (verified) {

                                    Account.initializeAccounts();

                                    thisAccount = new Account(username.getText(), password.getText());

                                    JOptionPane.showMessageDialog(null,
                                            String.format("Login Successful!\nWelcome %s!", thisAccount.name),
                                            "Learning Management System", JOptionPane.INFORMATION_MESSAGE);

                                    name = thisAccount.getFullName();
                                    type = thisAccount.getType();


                                } else {

                                    JOptionPane.showMessageDialog(null,
                                            "Invalid Credentials!\nTry Again!\n" +
                                                    "(Click Cancel to return to Main Menu)",
                                            "Learning Management System", JOptionPane.PLAIN_MESSAGE);

                                }

                            } else {

                                mainOption = 4;
                                break;

                            }

                        } while (!verified);    // Lets the user log in

                        if (mainOption == 4) {

                            break;

                        }

                        do {

                            String[] mainOptions = {"Account Settings", "Courses", "Exit"};

                            systemOption = JOptionPane.showOptionDialog(null,
                                    "Please Select Your Choice", String.format("%s: %s Menu", name, type),
                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                    mainOptions, mainOptions[2]);

                            switch (systemOption) {

                                case 0 -> {

                                    String[] accountOptions = {"Show Account Details", "Edit Account",
                                            "Delete Account"};

                                    userAccount = JOptionPane.showOptionDialog(null,
                                            "Please Select Your Choice",
                                            String.format("%s: Account Menu", name), JOptionPane.OK_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE, null,
                                            accountOptions, accountOptions[2]);

                                    switch (userAccount) {

                                        case 0 -> {

                                            Account.initializeAccounts();

                                            send.println("accountDetails");
                                            send.flush();

                                            send.println(thisAccount.getUsername());
                                            send.flush();
                                            send.println(thisAccount.getPassword());
                                            send.flush();

                                            JTextArea details = new JTextArea("Account Details\n");
                                            details.append(get.readLine() + "\n");
                                            details.append(get.readLine() + "\n");
                                            details.append(get.readLine() + "\n");
                                            details.append(get.readLine());

                                            details.setRows(5);
                                            details.setLineWrap(true);
                                            details.setEditable(false);
                                            JOptionPane.showMessageDialog(null,
                                                    details, String.format("%s: Details", name),
                                                    JOptionPane.INFORMATION_MESSAGE);

                                        }   // Show Account Details

                                        case 1 -> {

                                            Account.initializeAccounts();

                                            String[] choices = {"Name", "Username", "Password"};

                                            int choice = JOptionPane.showOptionDialog(null,
                                                    "What do you want to update?",
                                                    String.format("%s: Update Account", name),
                                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                    null, choices, choices[0]);

                                            if (choice == -1) {

                                                break;

                                            }

                                            JTextField update = new JTextField(String.format("Enter new %s",
                                                    choices[choice]));

                                            Object[] toUpdate = {String.format("New %s:", choices[choice]), update};

                                            boolean updated;

                                            do {

                                                int click = JOptionPane.showConfirmDialog(null, toUpdate,
                                                        String.format("%s: Update Account", name),
                                                        JOptionPane.OK_CANCEL_OPTION);

                                                if (click == JOptionPane.OK_OPTION) {

                                                    send.println("updateAccount");
                                                    send.flush();

                                                    send.println(thisAccount.getUsername());
                                                    send.flush();
                                                    send.println(thisAccount.getPassword());
                                                    send.flush();

                                                    send.println(choice);
                                                    send.flush();

                                                    send.println(update.getText());
                                                    send.flush();

                                                    updated = Boolean.parseBoolean(get.readLine());

                                                    System.out.println(updated);

                                                    if (updated) {

                                                        Account.initializeAccounts();

                                                        switch (choice) {

                                                            case 0 -> {

                                                                thisAccount = new Account(thisAccount.getUsername(),
                                                                        thisAccount.getPassword());

                                                                name = thisAccount.name;

                                                            }

                                                            case 1 -> thisAccount = new Account(update.getText(),
                                                                    thisAccount.getPassword());

                                                            case 2 -> thisAccount = new Account(thisAccount.getUsername(),
                                                                    update.getText());


                                                        }

                                                    } else {

                                                        JOptionPane.showMessageDialog(null,
                                                                String.format("Entered %s is invalid!", choices[choice])
                                                                , String.format("%s: Update Account", name),
                                                                JOptionPane.WARNING_MESSAGE);

                                                    }

                                                } else {

                                                    JOptionPane.showMessageDialog(null,
                                                            "Account Update Canceled!",
                                                            String.format("%s: Update Account", name),
                                                            JOptionPane.INFORMATION_MESSAGE);

                                                    updated = true;

                                                }

                                            } while (!updated);

                                        }   // Update Account

                                        case 2 -> {

                                            Account.initializeAccounts();

                                            int confirmDelete = JOptionPane.showConfirmDialog(null,
                                                    "Are You Sure to Delete Account?",
                                                    String.format("%s: Delete Account", name),
                                                    JOptionPane.OK_CANCEL_OPTION);

                                            if (confirmDelete == JOptionPane.OK_OPTION) {

                                                send.println("deleteAccount");
                                                send.flush();

                                                send.println(thisAccount.getUsername());
                                                send.flush();
                                                send.println(thisAccount.getPassword());
                                                send.flush();

                                                boolean deleted = Boolean.parseBoolean(get.readLine());

                                                if (deleted) {

                                                    Account.initializeAccounts();

                                                    JOptionPane.showMessageDialog(null,
                                                            "Account Deleted!",
                                                            String.format("%s: Delete Account", name),
                                                            JOptionPane.INFORMATION_MESSAGE);

                                                    systemOption = 2;

                                                }

                                            } else {

                                                JOptionPane.showMessageDialog(null,
                                                        "Delete Cancelled\n",
                                                        String.format("%s: Delete Account", name),
                                                        JOptionPane.INFORMATION_MESSAGE);

                                            }

                                        }   // Delete Account

                                    }

                                }

                                case 1 -> {

                                    if ("teacher".equals(thisAccount.getType())) {

                                        String[] teacherOptions = {"Select Course", "Add Course", "Delete Course", "Exit"};

                                        String teacherChoice;

                                        do {

                                            teacherChoice = (String) JOptionPane.showInputDialog(null,
                                                    "Select Option", String.format("%s: Teacher Menu", name),
                                                    JOptionPane.PLAIN_MESSAGE, null, teacherOptions,
                                                    null);

                                            if (teacherChoice == null) {

                                                break;

                                            }

                                            switch (teacherChoice) {

                                                case "Select Course" -> {

                                                    send.println("listCourses");
                                                    send.flush();

                                                    int num = Integer.parseInt(get.readLine());

                                                    String[] courses = new String[num + 1];

                                                    for (int i = 0; i < num; i++) {

                                                        courses[i] = get.readLine();

                                                    }

                                                    courses[courses.length - 1] = "Exit";


                                                    String courseChoice;

                                                    do {

                                                        courseChoice = (String) JOptionPane.showInputDialog(
                                                                null, "Select Option",
                                                                String.format("%s: Teacher Menu", name),
                                                                JOptionPane.PLAIN_MESSAGE, null, courses,
                                                                null);

                                                        if (courseChoice == null) {

                                                            break;

                                                        }

                                                        if (!"Exit".equals(courseChoice)) {

                                                            String courseName = courseChoice.substring(
                                                                    courseChoice.indexOf(" ") + 1);

                                                            Course thisCourse = new Course(courseName, q);

                                                            String[] courseMenuOptions = {"Course Details", "Select Quiz",
                                                                    "Add Quiz"};

                                                            int courseMenu = JOptionPane.showOptionDialog(null,
                                                                    "Please Select Your Choice",
                                                                    String.format("%s: %s Menu Options", name, courseName),
                                                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                                    null, courseMenuOptions, null);

                                                            switch (courseMenu) {

                                                                case 0 -> {

                                                                    send.println("courseDetails");
                                                                    send.flush();

                                                                    send.println(thisCourse.courseName);
                                                                    send.flush();

                                                                    JTextArea courseDetails = new JTextArea("Course Details:\n");
                                                                    courseDetails.append(get.readLine() + "\n");
                                                                    courseDetails.append(get.readLine() + "\n");
                                                                    courseDetails.append(get.readLine());

                                                                    courseDetails.setLineWrap(true);
                                                                    courseDetails.setEditable(false);

                                                                    JOptionPane.showMessageDialog(null,
                                                                            courseDetails,
                                                                            String.format("%s: %s Details", name, thisCourse.courseName),
                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                }   // Show the details of the course

                                                                case 1 -> {

                                                                    send.println("listQuizNames");
                                                                    send.println(thisCourse.courseName);

                                                                    int size = Integer.parseInt(get.readLine());

                                                                    String[] quizNames = new String[size];

                                                                    for (int i = 0; i < size; i++) {

                                                                        quizNames[i] = get.readLine();

                                                                    }

                                                                    String quizSelected = (String) JOptionPane.showInputDialog(
                                                                            null, "Select Quiz",
                                                                            String.format("%s: Quiz Menu", name),
                                                                            JOptionPane.PLAIN_MESSAGE, null, quizNames,
                                                                            null);

                                                                    if (quizSelected == null) {

                                                                        break;

                                                                    }

                                                                    String[] quizChoices = {"View Quiz", "Add Question",
                                                                    "Remove Question", "Remove Quiz", "View Quiz Submissions"};

                                                                    String quizChoice = (String) JOptionPane.showInputDialog(
                                                                            null, "Select Action:",
                                                                            String.format("%s: %s Menu", name, quizSelected),
                                                                            JOptionPane.PLAIN_MESSAGE, null, quizChoices,
                                                                            null);

                                                                    switch (quizChoice) {

                                                                        case "View Quiz" -> {

                                                                            send.println("printQuiz");
                                                                            send.flush();

                                                                            send.println(quizSelected);
                                                                            send.flush();

                                                                            send.println(thisAccount.getType());
                                                                            send.flush();

                                                                            String quiz = get.readLine();

                                                                            quiz = quizFormatter(quiz);

                                                                            JTextArea quizText = new JTextArea(quiz);

                                                                            JScrollPane scrollQuiz = new JScrollPane(quizText);
                                                                            quizText.setLineWrap(true);
                                                                            quizText.setWrapStyleWord(true);
                                                                            scrollQuiz.setPreferredSize(new Dimension(250, 250));

                                                                            JOptionPane.showMessageDialog(null,
                                                                                    scrollQuiz, String.format("%s: %s", courseName, quizSelected),
                                                                                    JOptionPane.PLAIN_MESSAGE);


                                                                        }       // See all questions for the quiz

                                                                        case "Add Question" -> {

                                                                            String[] qTypeChoice = {"T/F", "MCQ"};
                                                                            int qType = JOptionPane.showOptionDialog(null,
                                                                                    "Please Select Quiz Type",
                                                                                    String.format("%s: %s Question Type", name, quizSelected),
                                                                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                                                    null, qTypeChoice, null);

                                                                            if (qType == -1) {

                                                                                break;

                                                                            }

                                                                            JTextField question = new JTextField();
                                                                            Object[] inputQ = {"Question:", question};

                                                                            JTextField correctAns = new JTextField();
                                                                            Object[] correct = {"Correct Ans:", correctAns};

                                                                            int option1 = JOptionPane.showConfirmDialog(null, inputQ,
                                                                                    String.format("%s: %s Add Question", name, quizSelected),
                                                                                    JOptionPane.OK_CANCEL_OPTION);

                                                                            if (question.getText().isBlank()) {

                                                                                JOptionPane.showMessageDialog(null,
                                                                                        "Question Add Canceled",
                                                                                        String.format("%s: %s Add Question", name, quizSelected),
                                                                                        JOptionPane.WARNING_MESSAGE);

                                                                                break;

                                                                            }

                                                                            if (option1 == JOptionPane.OK_OPTION) {

                                                                                StringBuilder choices = new StringBuilder();

                                                                                if (qType == 1) {

                                                                                    JTextField numChoices = new JTextField();
                                                                                    JTextField enteredChoice = new JTextField();

                                                                                    Object[] inputNum = {"Number of Choices:", numChoices};
                                                                                    Object[] inputC = {"Enter Option:", enteredChoice};

                                                                                    int option2 = JOptionPane.showConfirmDialog(
                                                                                            null, inputNum,
                                                                                            String.format("%s: %s Add Question", name, quizSelected),
                                                                                            JOptionPane.OK_CANCEL_OPTION);

                                                                                    if (option2 == JOptionPane.OK_OPTION) {

                                                                                        int numQ;
                                                                                        try {
                                                                                            numQ = Integer.parseInt(numChoices.getText());
                                                                                        } catch (NumberFormatException e) {
                                                                                            JOptionPane.showMessageDialog(null,
                                                                                                    "Not a valid number!\n" +
                                                                                                            "Question Add Canceled",
                                                                                                    String.format("%s: %s Add Question", name, quizSelected),
                                                                                                    JOptionPane.PLAIN_MESSAGE);
                                                                                            break;
                                                                                        }


                                                                                        for (int i = 0; i < numQ; i++) {

                                                                                            int option3 = JOptionPane.showConfirmDialog(
                                                                                                    null, inputC,
                                                                                                    String.format("%s: %s Add Question", name, quizSelected),
                                                                                                    JOptionPane.OK_CANCEL_OPTION);

                                                                                            if (option3 == JOptionPane.OK_OPTION) {

                                                                                                if (enteredChoice.getText().isBlank()) {

                                                                                                    JOptionPane.showMessageDialog(null,
                                                                                                            "Question Add Canceled",
                                                                                                            String.format("%s: %s Add Question", name, quizSelected),
                                                                                                            JOptionPane.WARNING_MESSAGE);

                                                                                                    break;

                                                                                                }

                                                                                                choices.append(enteredChoice.getText());
                                                                                                choices.append("&");

                                                                                                enteredChoice.setText("");


                                                                                            }

                                                                                        }

                                                                                        if (choices.toString().split("&").length < numQ) {

                                                                                            JOptionPane.showMessageDialog(null,
                                                                                                    "Question Add Canceled",
                                                                                                    String.format("%s: %s Add Question", name, quizSelected),
                                                                                                    JOptionPane.WARNING_MESSAGE);

                                                                                            break;

                                                                                        }

                                                                                        int option3 = JOptionPane.showConfirmDialog(null, correct,
                                                                                                String.format("%s: %s Add Question", name, quizSelected),
                                                                                                JOptionPane.OK_CANCEL_OPTION);

                                                                                        if (option3 == JOptionPane.OK_OPTION) {

                                                                                            if (correctAns.getText().isBlank()) {

                                                                                                JOptionPane.showMessageDialog(null,
                                                                                                        "No Answer Entered!\nQuestion Add Canceled",
                                                                                                        String.format("%s: %s Add Question", name, quizSelected),
                                                                                                        JOptionPane.WARNING_MESSAGE);

                                                                                                break;
                                                                                            }


                                                                                        } else {

                                                                                            JOptionPane.showMessageDialog(null,
                                                                                                    "Question Add Canceled",
                                                                                                    String.format("%s: %s Add Question", name, quizSelected),
                                                                                                    JOptionPane.WARNING_MESSAGE);

                                                                                            break;

                                                                                        }

                                                                                    }
                                                                                } else {

                                                                                    int option3 = JOptionPane.showConfirmDialog(null, correct,
                                                                                            String.format("%s: %s Add Question", name, quizSelected),
                                                                                            JOptionPane.OK_CANCEL_OPTION);

                                                                                    if (option3 == JOptionPane.OK_OPTION) {

                                                                                        if (correctAns.getText().isBlank()) {

                                                                                            JOptionPane.showMessageDialog(null,
                                                                                                    "No Answer Entered!\nQuestion Add Canceled",
                                                                                                    String.format("%s: %s Add Question", name, quizSelected),
                                                                                                    JOptionPane.WARNING_MESSAGE);

                                                                                            break;
                                                                                        }


                                                                                    } else {

                                                                                        JOptionPane.showMessageDialog(null,
                                                                                                "Question Add Canceled",
                                                                                                String.format("%s: %s Add Question", name, quizSelected),
                                                                                                JOptionPane.WARNING_MESSAGE);

                                                                                        break;

                                                                                    }

                                                                                    choices.append("True&False&");


                                                                                }



                                                                                send.println("addQuestion");
                                                                                send.flush();

                                                                                send.println(quizSelected);
                                                                                send.flush();

                                                                                send.println(qType);
                                                                                send.flush();

                                                                                send.println(question.getText());
                                                                                send.flush();

                                                                                send.println(choices);
                                                                                send.flush();

                                                                                send.println(correctAns.getText());
                                                                                send.flush();

                                                                                boolean added = Boolean.parseBoolean(get.readLine());

                                                                                if (added) {

                                                                                    JOptionPane.showMessageDialog(null,
                                                                                            "Question Add Successful!",
                                                                                            String.format("%s: %s Add Question", name, quizSelected),
                                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                                } else {

                                                                                    JOptionPane.showMessageDialog(null,
                                                                                            "Question Add Canceled",
                                                                                            String.format("%s: %s Add Question", name, quizSelected),
                                                                                            JOptionPane.WARNING_MESSAGE);

                                                                                }



                                                                            } else {

                                                                                JOptionPane.showMessageDialog(null,
                                                                                        "Question Add Canceled",
                                                                                        String.format("%s: %s Add Question", name, quizSelected),
                                                                                        JOptionPane.WARNING_MESSAGE);

                                                                            }




                                                                        }    // Add a question to the quiz

                                                                        case "Remove Question" -> {

                                                                            JTextField qNumField = new JTextField();
                                                                            Object[] qNumBox = {"Question Number", qNumField};

                                                                            int option1 = JOptionPane.showConfirmDialog(null,
                                                                                    qNumBox,
                                                                                    String.format("%s: %s Remove Question", name, quizSelected),
                                                                                    JOptionPane.OK_CANCEL_OPTION);

                                                                            if (option1 == JOptionPane.OK_OPTION) {

                                                                                int qNum;

                                                                                try {

                                                                                    qNum = Integer.parseInt(qNumField.getText());

                                                                                } catch (NumberFormatException e) {

                                                                                    JOptionPane.showMessageDialog(null,
                                                                                            "Not a valid Question Number\n" +
                                                                                                    "Question Removal Canceled",
                                                                                            String.format("%s: %s Remove Question", name, quizSelected),
                                                                                            JOptionPane.WARNING_MESSAGE);
                                                                                    break;

                                                                                }

                                                                                send.println("deleteQuestion");
                                                                                send.flush();

                                                                                send.println(quizSelected);
                                                                                send.flush();

                                                                                send.println(qNum);
                                                                                send.flush();

                                                                                boolean removed = Boolean.parseBoolean(get.readLine());

                                                                                if (removed) {

                                                                                    JOptionPane.showMessageDialog(null,
                                                                                            "Question Removed!",
                                                                                            String.format("%s: %s Remove Question", name, quizSelected),
                                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                                } else {

                                                                                    JOptionPane.showMessageDialog(null,
                                                                                            "Question Removal Canceled",
                                                                                            String.format("%s: %s Remove Question", name, quizSelected),
                                                                                            JOptionPane.WARNING_MESSAGE);

                                                                                }

                                                                            } else {

                                                                                JOptionPane.showMessageDialog(null,
                                                                                        "Question Delete Canceled",
                                                                                        String.format("%s: %s Remove Question", name, quizSelected),
                                                                                        JOptionPane.WARNING_MESSAGE);

                                                                            }

                                                                        } // Remove a question from quiz

                                                                        case "Remove Quiz" -> {

                                                                            int option1 = JOptionPane.showConfirmDialog(
                                                                                    null,
                                                                                    String.format("Are you sure to delete %s?", quizSelected),
                                                                                    String.format("%s: Delete %s", name, quizSelected),
                                                                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

                                                                            if (option1 == JOptionPane.OK_OPTION) {

                                                                                send.println("deleteQuiz");
                                                                                send.flush();

                                                                                send.println(quizSelected);
                                                                                send.flush();

                                                                                boolean deleted = Boolean.parseBoolean(get.readLine());

                                                                                if (deleted) {

                                                                                    JOptionPane.showMessageDialog(null,
                                                                                            "Quiz Removed!",
                                                                                            String.format("%s: Remove %s", name, quizSelected),
                                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                                } else {

                                                                                    JOptionPane.showMessageDialog(null,
                                                                                            "Quiz Removal Canceled",
                                                                                            String.format("%s: Remove %s", name, quizSelected),
                                                                                            JOptionPane.WARNING_MESSAGE);

                                                                                }


                                                                            }

                                                                        }     // Delete entire quiz

                                                                        case "View Quiz Submissions" -> {

                                                                            send.println("quizSubmissions");
                                                                            send.flush();

                                                                            send.println(quizSelected);
                                                                            send.flush();

                                                                            int subs = Integer.parseInt(get.readLine());

                                                                            JTextArea viewSubs = new JTextArea("Quiz Submissions\n");

                                                                            for (int i = 0; i < subs; i++) {

                                                                                viewSubs.append(get.readLine());
                                                                                viewSubs.append("\n");

                                                                            }

                                                                            viewSubs.setLineWrap(true);
                                                                            viewSubs.setWrapStyleWord(true);
                                                                            JScrollPane scrollSubs = new JScrollPane(viewSubs);

                                                                            scrollSubs.setPreferredSize(new Dimension(250, 250));

                                                                            JOptionPane.showMessageDialog(null,
                                                                                    scrollSubs, String.format("%s: %s", courseName, quizSelected),
                                                                                    JOptionPane.PLAIN_MESSAGE);


                                                                        }   // View all submissions for the quiz

                                                                    }


                                                                }   // View all Quizzes in the course

                                                                case 2 -> {

                                                                    JTextField quizName = new JTextField();
                                                                    Object[] add = {"Enter Quiz Name:", quizName};

                                                                    int option1 = JOptionPane.showConfirmDialog(null,
                                                                            add, String.format("%s: %s Add Quiz", name, courseName),
                                                                            JOptionPane.OK_CANCEL_OPTION);

                                                                    if (option1 == JOptionPane.OK_OPTION) {

                                                                        if (quizName.getText().isBlank()) {

                                                                            JOptionPane.showMessageDialog(null,
                                                                                    "No Quiz Name Entered!\n" +
                                                                                            "Quiz Add Canceled",
                                                                                    String.format("%s: %s Add Quiz", name, courseName),
                                                                                    JOptionPane.WARNING_MESSAGE);

                                                                            break;

                                                                        }

                                                                        send.println("addQuiz");
                                                                        send.flush();

                                                                        send.println(courseName);
                                                                        send.flush();

                                                                        boolean added = Boolean.parseBoolean(get.readLine());

                                                                        if (added) {

                                                                            JOptionPane.showMessageDialog(null,
                                                                                    "Quiz Added Successfully",
                                                                                    String.format("%s: %s Add Quiz", name, courseName),
                                                                                    JOptionPane.INFORMATION_MESSAGE);

                                                                        } else {

                                                                            JOptionPane.showMessageDialog(null,
                                                                                    "Quiz Add Failed!",
                                                                                    String.format("%s: %s Add Quiz", name, courseName),
                                                                                    JOptionPane.WARNING_MESSAGE);

                                                                        }

                                                                    } else {

                                                                        JOptionPane.showMessageDialog(null,
                                                                                "Quiz Add Canceled",
                                                                                String.format("%s: %s Add Quiz", name, courseName),
                                                                                JOptionPane.WARNING_MESSAGE);

                                                                    }

                                                                }   // Add a quiz to the course

                                                                //TODO Teacher file upload


                                                            }

                                                        }



                                                    } while (!courseChoice.equals("Exit"));

                                                }

                                                case "Add Course" -> {

                                                    Course.initializeCourses();

                                                    JTextField courseName = new JTextField();
                                                    Object[] textField = {"Course Name:", courseName};

                                                    int entered = JOptionPane.showConfirmDialog(null,
                                                            textField, String.format("%s: Add Course", name),
                                                            JOptionPane.OK_CANCEL_OPTION);

                                                    if (courseName.getText().isBlank()) {

                                                        JOptionPane.showMessageDialog(null,
                                                                "Course Add Canceled\nNo Course Entered!",
                                                                String.format("%s: Add Course Menu", name),
                                                                JOptionPane.ERROR_MESSAGE);

                                                        break;

                                                    }

                                                    if (entered == JOptionPane.OK_OPTION) {

                                                        send.println("addCourse");
                                                        send.flush();

                                                        send.println(courseName.getText());
                                                        send.flush();

                                                        boolean added = Boolean.parseBoolean(get.readLine());

                                                        if (added) {

                                                            Course.initializeCourses();

                                                            JOptionPane.showMessageDialog(null,
                                                                    String.format("%s Added!", courseName.getText()),
                                                                    String.format("%s: Add Course Menu", name),
                                                                    JOptionPane.INFORMATION_MESSAGE);

                                                        } else {

                                                            JOptionPane.showMessageDialog(null,
                                                                    "Course Add Failed",
                                                                    String.format("%s: Add Course Menu", name),
                                                                    JOptionPane.INFORMATION_MESSAGE);

                                                        }

                                                    } else {

                                                        JOptionPane.showMessageDialog(null,
                                                                "Course Add Canceled",
                                                                String.format("%s: Add Course Menu", name),
                                                                JOptionPane.INFORMATION_MESSAGE);

                                                    }

                                                }

                                                case "Delete Course" -> {

                                                    Course.initializeCourses();

                                                    JTextField courseName = new JTextField();
                                                    Object[] textField = {"Course Name:", courseName};

                                                    int entered = JOptionPane.showConfirmDialog(null,
                                                            textField, String.format("%s: Remove Course", name),
                                                            JOptionPane.OK_CANCEL_OPTION);

                                                    if (courseName.getText().isBlank()) {

                                                        JOptionPane.showMessageDialog(null,
                                                                "Course Deletion Canceled\nNo Course Entered!",
                                                                String.format("%s: Remove Course Menu", name),
                                                                JOptionPane.INFORMATION_MESSAGE);

                                                    }

                                                    if (entered == JOptionPane.OK_OPTION) {

                                                        send.println("deleteCourse");
                                                        send.flush();

                                                        send.println(courseName.getText());
                                                        send.flush();

                                                        boolean removed = Boolean.parseBoolean(get.readLine());

                                                        if (removed) {

                                                            Course.initializeCourses();

                                                            JOptionPane.showMessageDialog(null,
                                                                    String.format("%s Removed!", courseName.getText()),
                                                                    String.format("%s: Remove Course Menu", name),
                                                                    JOptionPane.INFORMATION_MESSAGE);

                                                        } else {

                                                            JOptionPane.showMessageDialog(null,
                                                                    "Course Deletion Failed",
                                                                    String.format("%s: Remove Course Menu", name),
                                                                    JOptionPane.INFORMATION_MESSAGE);

                                                        }

                                                    } else {

                                                        JOptionPane.showMessageDialog(null,
                                                                "Course Delete Canceled",
                                                                String.format("%s: Remove Course Menu", name),
                                                                JOptionPane.INFORMATION_MESSAGE);

                                                    }

                                                }

                                                case "Exit" -> {}

                                            }

                                        } while (!"Exit".equals(teacherChoice));

                                    } else if ("student".equals(thisAccount.getType())) {

                                        String[] studentOptions = {"Select Course", "View Your Overall Score", "Exit"};

                                        String studentChoice;

                                        do {

                                            studentChoice = (String) JOptionPane.showInputDialog(null,
                                                    "Select Option", String.format("%s: Student Menu", name),
                                                    JOptionPane.PLAIN_MESSAGE, null, studentOptions,
                                                    null);

                                            if (studentChoice == null) {

                                                break;

                                            }

                                            switch (studentChoice) {

                                                case "Select Course" -> {

                                                    send.println("listCourses");
                                                    send.flush();

                                                    int num = Integer.parseInt(get.readLine());

                                                    String[] courses = new String[num + 1];

                                                    for (int i = 0; i < num; i++) {

                                                        courses[i] = get.readLine();

                                                    }

                                                    courses[courses.length - 1] = "Exit";

                                                    String courseChoice;

                                                    do {

                                                        courseChoice = (String) JOptionPane.showInputDialog(
                                                                null, "Select Option",
                                                                String.format("%s: Student Menu", name),
                                                                JOptionPane.PLAIN_MESSAGE, null, courses,
                                                                null);

                                                        if (courseChoice == null) {

                                                            break;

                                                        }

                                                        if (!"Exit".equals(courseChoice)) {

                                                            String courseName = courseChoice.substring(
                                                                    courseChoice.indexOf(" ") + 1);

                                                            Course thisCourse = new Course(courseName, q);

                                                            String[] courseMenuOptions = {"Course Details", "List Quizzes",
                                                                    "Your Course Grade", "Course Average"};

                                                            int courseMenu = JOptionPane.showOptionDialog(null,
                                                                    "Please Select Your Choice",
                                                                    String.format("%s: %s Menu Options", name, courseName),
                                                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                                                                    null, courseMenuOptions, null);

                                                            switch (courseMenu) {

                                                                case 0 -> {

                                                                    send.println("courseDetails");
                                                                    send.flush();

                                                                    send.println(thisCourse.courseName);
                                                                    send.flush();

                                                                    JTextArea courseDetails = new JTextArea("Course Details:\n");
                                                                    courseDetails.append(get.readLine() + "\n");
                                                                    courseDetails.append(get.readLine() + "\n");
                                                                    courseDetails.append(get.readLine());

                                                                    courseDetails.setRows(4);
                                                                    courseDetails.setLineWrap(true);
                                                                    courseDetails.setEditable(false);

                                                                    JOptionPane.showMessageDialog(null,
                                                                            courseDetails,
                                                                            String.format("%s: %s Details", name, thisCourse.courseName),
                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                }

                                                                case 1 -> {

                                                                    send.println("listQuizNames");
                                                                    send.println(thisCourse.courseName);

                                                                    int size = Integer.parseInt(get.readLine());

                                                                    String[] quizNames = new String[size];

                                                                    for (int i = 0; i < size; i++) {

                                                                        quizNames[i] = get.readLine();

                                                                    }

                                                                    String quizSelected = (String) JOptionPane.showInputDialog(
                                                                            null, "Select Quiz",
                                                                            String.format("%s: Quiz Menu", name),
                                                                            JOptionPane.PLAIN_MESSAGE, null, quizNames,
                                                                            null);

                                                                    if (quizSelected == null) {

                                                                        break;

                                                                    }

                                                                    String[] quizChoices = {"Attempt Quiz", "View Quiz Average"};


                                                                } //TODO

                                                                case 2 -> {

                                                                    GradeBook.initializeStudentGradebook();

                                                                    send.println("studentInCourse");
                                                                    send.flush();

                                                                    send.println(name);
                                                                    send.flush();

                                                                    send.println(thisCourse.courseName);
                                                                    send.flush();

                                                                    double courseGrade = Double.parseDouble(get.readLine());

                                                                    JOptionPane.showMessageDialog(null,
                                                                            String.format("Your Grade in %s is: %.2f",
                                                                                    thisCourse.courseName, courseGrade),
                                                                            String.format("%s: Course Grade", name),
                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                }

                                                                case 3 -> {

                                                                    GradeBook.initializeStudentGradebook();

                                                                    send.println("courseAverage");
                                                                    send.flush();

                                                                    send.println(thisCourse.courseName);
                                                                    send.flush();

                                                                    double courseGrade = Double.parseDouble(get.readLine());

                                                                    JOptionPane.showMessageDialog(null,
                                                                            String.format("Your Grade in %s is: %.2f",
                                                                                    thisCourse.courseName, courseGrade),
                                                                            String.format("%s: Course Grade", name),
                                                                            JOptionPane.INFORMATION_MESSAGE);

                                                                }

                                                            }
                                                        }

                                                    } while (!courseChoice.equals("Exit"));


                                                }

                                                case "View Your Overall Score" -> {

                                                    send.println("studentAverage");

                                                    double overallScore = Double.parseDouble(get.readLine());

                                                    JOptionPane.showMessageDialog(null,
                                                            String.format("Your Overall Score is: %.2f", overallScore),
                                                            String.format("%s: Overall Score", name),
                                                            JOptionPane.INFORMATION_MESSAGE);

                                                }

                                                case "Exit" -> {
                                                }

                                            }


                                        } while (!"Exit".equals(studentChoice));

                                    }

                                }

                                case 2 -> {}

                                default -> mainOption = 2;


                            }

                        } while (systemOption != 2);

                    }

                    case 1 -> {

                        boolean created;

                        do {

                            String[] typeOptions = {"Student", "Teacher"};

                            int chosenType = JOptionPane.showOptionDialog(null, "Select Account Type:",
                                    "Learning Management System", JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE, null, typeOptions, typeOptions[0]);

                            String type = "";

                            switch (chosenType) {

                                case 0 -> type = "student";

                                case 1 -> type = "teacher";

                            }

                            if (type.equals("")) {
                                mainOption = 2;
                                break;
                            }

                            JTextField username = new JTextField();
                            JTextField password = new JPasswordField();
                            JTextField name = new JTextField();
                            Object[] input = {"Name:", name, "Username:", username, "Password:", password};

                            int option = JOptionPane.showConfirmDialog(null, input,
                                    "Enter Account Details", JOptionPane.OK_CANCEL_OPTION);

                            if (option == JOptionPane.OK_OPTION) {

                                send.println("newAccount");
                                send.flush();

                                send.println(type);
                                send.flush();

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

                            } else if (option == JOptionPane.CANCEL_OPTION) {

                                mainOption = 2;
                                created = true;

                            } else {

                                break;

                            }

                        } while (!created);

                    } // create a new account

                    case 2 -> {
                    }

                    default -> mainOption = 2;

                }

            } while (mainOption != 2);

            JOptionPane.showMessageDialog(null, "Thank You for Using our System!",
                    "Learning Management System", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
