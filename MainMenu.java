import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import javax.swing.*;
import java.io.*;
import java.net.*;
import static javax.swing.JOptionPane.YES_OPTION;

/**
 * Project 4 - Main Menu
 * <p>
 * This class is the interface between all the databses and the users. It invokes all the relevant methods implemented
 * in the other classes as need, and also displays to the user whatever they may need to navigate through all the menus.
 *
 * @author Group 66, L16
 * @version April 11, 2022
 */

public class MainMenu {

    private static String [] mainOptions = {"1", "2", "3"};
    private static String [] teacherOptions = {"1","2", "3"};
    private static String [] teacherAccounts = {"1", "2"};
    private static String [] quizOptions = {"1", "2", "3", "4", "5"};

    public static void main(String[] args) {

        try {
            Account.initializeAccounts();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading the Data!.", "Goodbye",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int mainOption;     // user's choice in main menu
        int teacherOption;  // Teacher's options after logging in
        int teacherAccount; // Teacher's account settings
        int studentOption;  // Student's options after logging in
        int studentAccount;  // Student's account settings
        int teacherQuizChoice;      // teacher's choice in quiz menu
        int quizMenuChoiceTeacher;  // Teacher's choice to manipulate quizzes
        int studentQuizChoice;      // student's choice in quiz menu
        int studentCourseChoice;    // student's choice for course
        int quizMenuChoiceStudent;   // student's choice for quizzes (list or view attempts)

        JOptionPane.showMessageDialog(null, "Learning Management System", "Welcome",
                JOptionPane.INFORMATION_MESSAGE);

        do {
            mainOption = (int) JOptionPane.showInputDialog(null,
                    "Main Menu: \n1) Log In \n2) Sign Up \n3) Exit",
                    "Menu", JOptionPane.QUESTION_MESSAGE,
                    null, mainOptions, mainOptions[0]);

            if (mainOption == 1) {

                String username;
                String password;
                Account thisAccount = null;    // account for this user

                do {

                    username = JOptionPane.showInputDialog(null, "Enter Username: ",
                            "Account", JOptionPane.QUESTION_MESSAGE);

                    password = JOptionPane.showInputDialog(null, "Enter Password: ",
                            "Account", JOptionPane.QUESTION_MESSAGE);

                    if (Account.isValidCredential(username, password)) {

                        thisAccount = new Account(username, password);
                        JOptionPane.showMessageDialog(null, "Login Succesful!", "Log in",
                                JOptionPane.INFORMATION_MESSAGE);

                    } else {

                        JOptionPane.showMessageDialog(null, "Invalid Credentials! /n Try Again", "Goodbye",
                                JOptionPane.ERROR_MESSAGE);

                    }

                } while (!Account.isValidCredential(username, password));

                if (thisAccount.getType().equals("teacher")) {

                    do {

                        teacherOption = (int) JOptionPane.showInputDialog(null,
                                "Teacher's Menu \n1) Account Settings \n2) Quizzes \n3) Exit",
                                "Menu", JOptionPane.QUESTION_MESSAGE,
                                null, teacherOptions, teacherOptions[0]);

                        if (teacherOption == 1) {

                            System.out.println("Teacher Account Menu \n1) Edit Account \n2) Delete Account");
                            teacherAccount = (int) JOptionPane.showInputDialog(null,
                                    "Teacher's Account Menu \n1) Edit Account \n2) Delete Account",
                                    "Menu", JOptionPane.QUESTION_MESSAGE,
                                    null, teacherAccounts, teacherAccounts[0]);

                            switch (teacherAccount) {

                                case 1 -> {

                                    System.out.println("1) Update Username \n2) Update Password \n3) Update Name");
                                    int choice = (int) JOptionPane.showInputDialog(null,
                                            "1) Update Username \n2) Update Password \n3) Update Name",
                                            "Menu", JOptionPane.QUESTION_MESSAGE,
                                            null, teacherOptions, teacherOptions[0]);    // user's choice
                                    thisAccount.updateAccount(scanner, choice);

                                }   // update account

                                case 2 -> {

                                    int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter 1 to confirm, anything else to cancel!",
                                            "Teacher", JOptionPane.QUESTION_MESSAGE));

                                    if (choice == 1) {

                                        thisAccount.deleteAccount();

                                    }

                                    teacherOption = 3;      // sends teacher to main menu

                                }   // delete account

                            }

                        } else if (teacherOption == 2) {

                            String quizFile = JOptionPane.showInputDialog(null, "Please Enter Quiz Filename: ",
                                    "Quiz", JOptionPane.QUESTION_MESSAGE);

                            Quiz quizzes;

                            try {
                                quizzes = new Quiz(quizFile);
                                Course.initializeCourses();
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, "Error Reading Quiz File!", "Goodbye",
                                        JOptionPane.ERROR_MESSAGE);
                                break;
                            }

                            do {

                                JOptionPane.showMessageDialog(null, "Teacher's Quiz Menu", "Teacher",
                                        JOptionPane.INFORMATION_MESSAGE);
                                teacherQuizChoice = (int) JOptionPane.showInputDialog(null,
                                        "1) Select Course \n2) Add Course \n3) Exit",
                                        "Menu", JOptionPane.QUESTION_MESSAGE,
                                        null, teacherOptions, teacherOptions[0]);

                                switch (teacherQuizChoice) {

                                    case 1 -> {

                                        Course.listCourses();

                                        int courseChoice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Course Number: ",
                                                "Teacher", JOptionPane.QUESTION_MESSAGE));

                                        String courseName = Course.allCourses.get(courseChoice - 1);

                                        Course thisCourse = new Course(courseName);

                                        quizMenuChoiceTeacher = (int) JOptionPane.showInputDialog(null,
                                                "1) List Course Details \n2) Add Quiz File \n3) Add Question \n4) Edit Quiz\n" +
                                                        "5) Remove Quiz",
                                                "Menu", JOptionPane.QUESTION_MESSAGE,
                                                null, quizOptions, quizOptions[0]);

                                        switch (quizMenuChoiceTeacher) {

                                            case 1 -> System.out.println(thisCourse);   // prints toString of course

                                            case 2 -> {

                                                int added = Integer.parseInt(JOptionPane.showInputDialog(null, "How many Questions are in the File?",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                String filename = (JOptionPane.showInputDialog(null, "Enter Filename:",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                Quiz.updateQuizFile(filename, thisCourse, added);
                                                JOptionPane.showMessageDialog(null, "Quiz Successfully Added!", "Welcome",
                                                        JOptionPane.INFORMATION_MESSAGE);

                                                thisCourse.updateCourseFile();

                                            }       // Adds questions from a  file to the quiz databse

                                            case 3 -> {

                                                String quizType;

                                                do {
                                                    quizType = (JOptionPane.showInputDialog(null, "Enter Quiz Type [MCQ / TF]",
                                                            "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                    if ((quizType.equals("MCQ")) || (quizType.equals("TF"))) {

                                                        break;

                                                    } else {

                                                        JOptionPane.showMessageDialog(null, "Invalid Choice!\n" + "Choice are case sensitive!", "Goodbye",
                                                                JOptionPane.ERROR_MESSAGE);

                                                    }

                                                } while(true);  // Get quiz type

                                                String quizName = (JOptionPane.showInputDialog(null, "Enter Name for Quiz:",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                String question = (JOptionPane.showInputDialog(null, "Enter Question:",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                String correctAns = (JOptionPane.showInputDialog(null, "Enter Correct Answer:",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));


                                                if (quizType.equals("MCQ")) {

                                                    int numQues = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Number of Choices: ",
                                                            "Teacher", JOptionPane.QUESTION_MESSAGE));

                                                    ArrayList<String> choices = new ArrayList<>();

                                                    for (int i = 0; i < numQues; i++) {

                                                        String message = JOptionPane.showInputDialog(null, "Enter Option "+ (i + 1), "Welcome", JOptionPane.QUESTION_MESSAGE);

                                                        choices.add(message);

                                                    }


                                                    if (quizzes.addQuiz(courseName, quizType, quizName, question,
                                                            choices, correctAns)) {

                                                        JOptionPane.showMessageDialog(null, "Successfully Added!", "Welcome",
                                                                JOptionPane.INFORMATION_MESSAGE);
                                                        Course.totalQuizzes.set(thisCourse.courseNumber,
                                                                thisCourse.numQuizzes += 1);

                                                        thisCourse.updateCourseFile();

                                                    } else {

                                                        JOptionPane.showMessageDialog(null, "Error Adding Quiz!", "Goodbye",
                                                                JOptionPane.ERROR_MESSAGE);

                                                    }

                                                } else {

                                                    if (quizzes.addQuiz(courseName, quizType, quizName, question,
                                                            correctAns)) {

                                                        JOptionPane.showMessageDialog(null, "Successfully Added!", "Welcome",
                                                                JOptionPane.INFORMATION_MESSAGE);
                                                        Course.totalQuizzes.set(thisCourse.courseNumber,
                                                                thisCourse.numQuizzes += 1);

                                                        thisCourse.updateCourseFile();

                                                    } else {

                                                        JOptionPane.showMessageDialog(null, "Error Adding Quiz!", "Goodbye",
                                                                JOptionPane.ERROR_MESSAGE);


                                                    }

                                                }


                                            }       // Adds a single question

                                            case 4 -> {

                                                String quizType;

                                                do {
                                                    quizType = (JOptionPane.showInputDialog(null, "Enter Quiz Type [MCQ / TF]",
                                                            "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                    if ((quizType.equals("MCQ")) || (quizType.equals("TF"))) {

                                                        break;

                                                    } else {

                                                        JOptionPane.showMessageDialog(null, "Invalid Choice!\n" + "Choice are case sensitive!", "Goodbye",
                                                                JOptionPane.ERROR_MESSAGE);

                                                    }

                                                } while(true);  // Get quiz type

                                                String quizName = (JOptionPane.showInputDialog(null, "Enter Name for Quiz:",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                String question = (JOptionPane.showInputDialog(null, "Enter Question:",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                String correctAns = (JOptionPane.showInputDialog(null, "Enter Correct Answer:",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));


                                                if (quizType.equals("MCQ")) {

                                                    int numChoice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Number of Choices to Add: ",
                                                            "Teacher", JOptionPane.QUESTION_MESSAGE));

                                                    ArrayList<String> choices = new ArrayList<>();

                                                    for (int i = 0; i < numChoice; i++) {

                                                        String message = JOptionPane.showInputDialog(null, "Enter Option "+ (i + 1), "Welcome", JOptionPane.QUESTION_MESSAGE);

                                                        choices.add(message);

                                                    }


                                                    if (quizzes.editQuiz(courseName, quizType, quizName, question,
                                                            choices, correctAns)) {

                                                        JOptionPane.showMessageDialog(null, "Successfully Added!", "Welcome",
                                                                JOptionPane.INFORMATION_MESSAGE);
                                                        Course.totalQuizzes.set(thisCourse.courseNumber, thisCourse.numQuizzes += 1);

                                                        thisCourse.updateCourseFile();



                                                    } else {

                                                        JOptionPane.showMessageDialog(null, "Error Adding Quiz!", "Goodbye",
                                                                JOptionPane.ERROR_MESSAGE);

                                                    }

                                                } else {

                                                    if (quizzes.editQuiz(courseName, quizType, quizName, question,
                                                            correctAns)) {

                                                        JOptionPane.showMessageDialog(null, "Successfully Added!", "Welcome",
                                                                JOptionPane.INFORMATION_MESSAGE);

                                                        thisCourse.updateCourseFile();

                                                    } else {

                                                        JOptionPane.showMessageDialog(null, "Error Adding Quiz!", "Goodbye",
                                                                JOptionPane.ERROR_MESSAGE);

                                                    }

                                                }

                                            }       // Edits a specific quiz

                                            case 5 -> {

                                                String quizName = (JOptionPane.showInputDialog(null, "Enter Name for Quiz:",
                                                        "Quiz", JOptionPane.QUESTION_MESSAGE));

                                                quizzes.deleteQuiz(quizName);
                                                Course.totalQuizzes.set(thisCourse.courseNumber,
                                                        thisCourse.numQuizzes -= 1);

                                                thisCourse.updateCourseFile();

                                            }       // Removes a specific quiz

                                            default -> JOptionPane.showMessageDialog(null, "Invalid Choice!", "Goodbye",
                                                    JOptionPane.ERROR_MESSAGE);

                                        }


                                    }

                                    case 2 -> {

                                        Course addedCourse = new Course(scanner);

                                        addedCourse.updateCourseFile();
                                        JOptionPane.showMessageDialog(null, "Course Added Successfully", "Welcome",
                                                JOptionPane.INFORMATION_MESSAGE);

                                    }

                                    case 3 -> {}        // do nothing if user exits

                                    default -> JOptionPane.showMessageDialog(null, "Invalid Choice!", "Goodbye",
                                            JOptionPane.ERROR_MESSAGE);

                                }

                            } while (teacherQuizChoice != 3);

                        } else if (teacherOption != 3) {

                            JOptionPane.showMessageDialog(null, "Invalid Choice!", "Goodbye",
                                    JOptionPane.ERROR_MESSAGE);

                        }

                    } while (teacherOption != 3);   // Teacher's menu

                } else if (thisAccount.getType().equals("student")) {

                    do {

                        studentOption = (int) JOptionPane.showInputDialog(null,
                                " Student's Menu \n1) Account Settings \n2) Quizzes \n3) Exit",
                                "Menu", JOptionPane.QUESTION_MESSAGE,
                                null, teacherOptions, teacherOptions[0]);

                        if (studentOption == 1) {

                            studentAccount = (int) JOptionPane.showInputDialog(null,
                                    "Student Account Menu \n1) Edit Account \n2) Delete Account",
                                    "Menu", JOptionPane.QUESTION_MESSAGE,
                                    null, teacherAccounts, teacherAccounts[0]);

                            switch (studentAccount) {

                                case 1 -> {

                                    System.out.println("1) Update Username \n2) Update Password \n3) Update Name");
                                    int choice = (int) JOptionPane.showInputDialog(null,
                                            "1) Update Username \n2) Update Password \n3) Update Name",
                                            "Menu", JOptionPane.QUESTION_MESSAGE,
                                            null, teacherOptions, teacherOptions[0]);    // user's choice
                                    thisAccount.updateAccount(scanner, choice);

                                }   // update account

                                case 2 -> {

                                    int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter 1 to confirm, anything else to cancel!",
                                            "Teacher", JOptionPane.QUESTION_MESSAGE));

                                    if (choice == 1) {

                                        thisAccount.deleteAccount();

                                    }

                                    studentOption = 3;      // sends student to main menu

                                }   // delete account

                            }

                        } else if (studentOption == 2) {

                            Course.initializeStudentGradebook();

                            do {

                                JOptionPane.showMessageDialog(null, "Student Quiz Menu", "Welcome",
                                        JOptionPane.INFORMATION_MESSAGE);
                                studentQuizChoice = (int) JOptionPane.showInputDialog(null,
                                        "1) Select Course\n2) View Overall Grade\n3) View Standing \n4) View Class Average\n" +
                                                "5) Exit",
                                        "Menu", JOptionPane.QUESTION_MESSAGE,
                                        null, quizOptions, quizOptions[0]);
                                scanner.nextLine();

                                String quizFile = "quiz_database.txt";

                                Quiz quizzes;

                                try {
                                    quizzes = new Quiz(quizFile);
                                    Course.initializeCourses();
                                } catch (IOException e) {
                                    JOptionPane.showMessageDialog(null, "Error Reading Quiz File!", "Goodbye",
                                            JOptionPane.ERROR_MESSAGE);
                                    break;
                                }


                                switch (studentQuizChoice) {

                                    case 1 -> {

                                        Course.listCourses();

                                        System.out.println("Select Course Number");
                                        studentCourseChoice = scanner.nextInt();
                                        scanner.nextLine();

                                        String courseName = Course.allCourses.get(studentCourseChoice - 1);
                                        Course thisCourse = new Course(courseName);

                                        int seed = thisAccount.getAccountSeed();    // gets seed for student
                                        Random random = new Random(seed);   // creates a random object
                                        int randomIndex;

                                        do {

                                            System.out.println("1) Attempt Assigned Quiz\n2) View All Assigned Quizzes" +
                                                    "\n3) View Total Attempts\n4) Exit");

                                            quizMenuChoiceStudent = scanner.nextInt();
                                            scanner.nextLine();

                                            switch (quizMenuChoiceStudent) {

                                                case 1 -> {

                                                    randomIndex = random.nextInt(thisCourse.courseQuizzes.size());

                                                    ArrayList<String> assignedQuiz =
                                                            thisCourse.courseQuizzes.get(randomIndex);

                                                    System.out.println("Assigned Quiz:");
                                                    quizzes.printQuiz(assignedQuiz.get(2), "student");

                                                    System.out.println("Enter your answer:");
                                                    String answerChoice = scanner.nextLine();

                                                    thisCourse.courseAttempts += 1;
                                                    Course.totalAttempts.set(thisCourse.courseNumber,
                                                            thisCourse.courseAttempts);

                                                    int studentGradebookNum = Course.allGradebookStudents.indexOf(
                                                            thisAccount.name);

                                                    double currentStudentGrade = Course.allStudentGrades.get(
                                                            studentGradebookNum);
                                                    int currentStudentAttempts = Course.allStudentAttempts.get(
                                                            studentGradebookNum);

                                                    Course.allStudentAttempts.set(studentGradebookNum,
                                                            currentStudentAttempts + 1);

                                                    double newGrade;

                                                    if (assignedQuiz.get(assignedQuiz.size() - 1).equals(answerChoice)){

                                                        JOptionPane.showMessageDialog(null, "Correct Answer!", "Welcome",
                                                                JOptionPane.INFORMATION_MESSAGE);

                                                        newGrade = ((currentStudentAttempts * currentStudentGrade
                                                        ) + 100) / (currentStudentAttempts + 1);

                                                        Course.allStudentGrades.set(studentGradebookNum, newGrade);



                                                    } else {

                                                        JOptionPane.showMessageDialog(null, "Incorrect Answer", "Welcome",
                                                                JOptionPane.INFORMATION_MESSAGE);

                                                        newGrade = ((currentStudentAttempts * currentStudentGrade
                                                        )) / (currentStudentAttempts + 1);

                                                        Course.allStudentGrades.set(studentGradebookNum, newGrade);

                                                    }

                                                    thisCourse.updateCourseFile();
                                                    Course.updateStudentGradebook();


                                                }   // Lets student attempt quiz


                                                case 2 -> {

                                                    quizzes.printCourseQuizzes(thisCourse);

                                                }   // prints quizzes for a specific course

                                                case 3 -> {

                                                    int totalAttempts = thisCourse.getTotalAttempts();

                                                    JOptionPane.showMessageDialog(null, "Student Attempts for " + this.courseName + "Quizzes: " + totalAttempts + "\n" , "Welcome",
                                                            JOptionPane.INFORMATION_MESSAGE);

                                                }

                                                case 4-> {}     // do nothing if user chooses to quit

                                                default -> JOptionPane.showMessageDialog(null, "Invalid Choice!", "Goodbye",
                                                        JOptionPane.ERROR_MESSAGE);

                                            }

                                        } while (quizMenuChoiceStudent != 4);

                                    }

                                    case 2 -> {

                                        double studentGrade = Course.allStudentGrades.get(
                                                Course.allGradebookStudents.indexOf(thisAccount.name));

                                        JOptionPane.showMessageDialog(null, "Your Average Grade is: \n" + studentGrade + "%%\n", "Welcome",
                                                JOptionPane.INFORMATION_MESSAGE);

                                    }       // Show overall grade

                                    case 3 -> {

                                        double studentGrade = Course.allStudentGrades.get(
                                                Course.allGradebookStudents.indexOf(thisAccount.name));
                                        int studentRank = Course.calculateStanding(studentGrade);
                                        int totalStudents = Course.allGradebookStudents.size();

                                        JOptionPane.showMessageDialog(null, "You rank " + studentRank + "amongst" + totalStudents + "students.\n", "Welcome",
                                                JOptionPane.INFORMATION_MESSAGE);
                                    }       // Shows student's rank

                                    case 4 -> {

                                        double averageScore = Course.calculateOverallAverage();
                                        
                                        JOptionPane.showMessageDialog(null, "The Class Average is " + averageScore + "\n", "Welcome",
                                                JOptionPane.INFORMATION_MESSAGE);

                                    }       // Shows overall class average

                                    case 5 -> {}          // does nothing if user decides to exit

                                    default -> {
                                        JOptionPane.showMessageDialog(null, "Inavlid Choice", "Goodbye",
                                                JOptionPane.ERROR_MESSAGE);
                                    }      // invalid choice

                                }


                            } while (studentQuizChoice != 5);

                        } else if (studentOption != 3) {

                            JOptionPane.showMessageDialog(null, "Invalid Option!", "Goodbye",
                                    JOptionPane.ERROR_MESSAGE);

                        }

                    } while (studentOption != 3);

                }


            } else if (mainOption == 2) {


                String type = "";

                System.out.println("Select User Type:\n1) Teacher\n2) Student");
                String typeChoice = scanner.nextLine();

                do {

                    if (typeChoice.equals("1")) {

                        type = "teacher";

                    } else if (typeChoice.equals("2")) {

                        type = "student";

                    }

                } while (type.equals(""));  // Gets choice for type of user

                Account newAccount = new Account(scanner, type);
                
                JOptionPane.showMessageDialog(null, "Successfully created account for" + newAccount.getFullName() + "\n", "Welcome",
                        JOptionPane.INFORMATION_MESSAGE);





            } else if (mainOption != 3){

                JOptionPane.showMessageDialog(null, "Invalid Option!", "Goodbye",
                        JOptionPane.ERROR_MESSAGE);

            }

        } while (mainOption != 3);
        
        JOptionPane.showMessageDialog(null, "Program Closed!", "Welcome",
                JOptionPane.INFORMATION_MESSAGE);

    }

}
