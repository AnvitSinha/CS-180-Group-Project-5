import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

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

    public static void main(String[] args) {

        try {
            Account.initializeAccounts();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred loading data!");
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

        Scanner scanner = new Scanner(System.in);

        System.out.println("Learning Management System");

        do {
            System.out.println("Main Menu: \n1) Log In \n2) Sign Up \n3) Exit");

            mainOption = scanner.nextInt();
            scanner.nextLine();

            if (mainOption == 1) {

                String username;
                String password;
                Account thisAccount = null;    // account for this user

                do {

                    System.out.println("Enter Username: ");
                    username = scanner.nextLine();

                    System.out.println("Enter Password: ");
                    password = scanner.nextLine();

                    if (Account.isValidCredential(username, password)) {

                        thisAccount = new Account(username, password);
                        System.out.println("Login Successful!\n");

                    } else {

                        System.out.println("Invalid Credentials! \nTry Again!");

                    }

                } while (!Account.isValidCredential(username, password));

                if (thisAccount.getType().equals("teacher")) {

                    do {

                        System.out.println("Teacher's Menu\n1) Account Settings\n2) Quizzes\n3) Exit");
                        teacherOption = scanner.nextInt();
                        scanner.nextLine();

                        if (teacherOption == 1) {

                            System.out.println("Teacher Account Menu \n1) Edit Account \n2) Delete Account");
                            teacherAccount = scanner.nextInt();
                            scanner.nextLine();

                            switch (teacherAccount) {

                                case 1 -> {

                                    System.out.println("1) Update Username \n2) Update Password \n3) Update Name");
                                    int choice = scanner.nextInt();     // user's choice
                                    scanner.nextLine();
                                    thisAccount.updateAccount(scanner, choice);

                                }   // update account

                                case 2 -> {

                                    System.out.println("Enter 1 to confirm, anything else to cancel!");
                                    int choice = scanner.nextInt();
                                    scanner.nextLine();

                                    if (choice == 1) {

                                        thisAccount.deleteAccount();

                                    }

                                    teacherOption = 3;      // sends teacher to main menu

                                }   // delete account

                            }

                        } else if (teacherOption == 2) {

                            System.out.println("Please Enter Quiz Filename: ");
                            String quizFile = scanner.nextLine();

                            Quiz quizzes;

                            try {
                                quizzes = new Quiz(quizFile);
                                Course.initializeCourses();
                            } catch (IOException e) {
                                System.out.println("Error Reading Quiz File!");
                                break;
                            }

                            do {

                                System.out.println("Teacher's Quiz Menu");
                                System.out.println("1) Select Course \n2) Add Course \n3) Exit");

                                teacherQuizChoice = scanner.nextInt();
                                scanner.nextLine();

                                switch (teacherQuizChoice) {

                                    case 1 -> {

                                        Course.listCourses();

                                        System.out.println("Enter Course Number: ");
                                        int courseChoice = scanner.nextInt();
                                        scanner.nextLine();

                                        String courseName = Course.allCourses.get(courseChoice - 1);

                                        Course thisCourse = new Course(courseName);

                                        System.out.println("1) List Course Details\n2) Add Quiz File" +
                                                "\n3) Add Question\n4) Edit Quiz\n5) Remove Quiz");

                                        quizMenuChoiceTeacher = scanner.nextInt();
                                        scanner.nextLine();

                                        switch (quizMenuChoiceTeacher) {

                                            case 1 -> System.out.println(thisCourse);   // prints toString of course

                                            case 2 -> {

                                                System.out.println("How many Questions are in the File?");
                                                int added = scanner.nextInt();
                                                scanner.nextLine();

                                                System.out.println("Enter Filename:");
                                                String filename = scanner.nextLine();

                                                Quiz.updateQuizFile(filename, thisCourse, added);
                                                System.out.println("Quiz Successfully Added!");

                                                thisCourse.updateCourseFile();

                                            }       // Adds questions from a  file to the quiz databse

                                            case 3 -> {

                                                String quizType;

                                                do {
                                                    System.out.println("Enter Quiz Type [MCQ / TF]");
                                                    quizType = scanner.nextLine();

                                                    if ((quizType.equals("MCQ")) || (quizType.equals("TF"))) {

                                                        break;

                                                    } else {

                                                        System.out.println("Invalid Choice!\n" +
                                                                "Choices are case sensitive!");

                                                    }

                                                } while(true);  // Get quiz type


                                                System.out.println("Enter Name for Quiz:");
                                                String quizName = scanner.nextLine();

                                                System.out.println("Enter Question:");
                                                String question = scanner.nextLine();

                                                System.out.println("Enter Correct Answer:");
                                                String correctAns = scanner.nextLine();


                                                if (quizType.equals("MCQ")) {

                                                    System.out.println("Enter Number of Choices:");
                                                    int numQues = scanner.nextInt();
                                                    scanner.nextLine();

                                                    ArrayList<String> choices = new ArrayList<>();

                                                    for (int i = 0; i < numQues; i++) {

                                                        System.out.printf("Enter Option %d", i + 1);
                                                        choices.add(scanner.nextLine());

                                                    }


                                                    if (quizzes.addQuiz(courseName, quizType, quizName, question,
                                                            choices, correctAns)) {

                                                        System.out.println("Successfully Added!");
                                                        Course.totalQuizzes.set(thisCourse.courseNumber,
                                                                thisCourse.numQuizzes += 1);

                                                        thisCourse.updateCourseFile();

                                                    } else {

                                                        System.out.println("Error Adding Quiz!");

                                                    }

                                                } else {

                                                    if (quizzes.addQuiz(courseName, quizType, quizName, question,
                                                            correctAns)) {

                                                        System.out.println("Successfully Added!");
                                                        Course.totalQuizzes.set(thisCourse.courseNumber,
                                                                thisCourse.numQuizzes += 1);

                                                        thisCourse.updateCourseFile();

                                                    } else {

                                                        System.out.println("Error Adding Quiz!");

                                                    }

                                                }


                                            }       // Adds a single question

                                            case 4 -> {

                                                String quizType;

                                                do {
                                                    System.out.println("Enter Quiz Type [MCQ / TF]");
                                                    quizType = scanner.nextLine();

                                                    if ((quizType.equals("MCQ")) || (quizType.equals("TF"))) {

                                                        break;

                                                    } else {

                                                        System.out.println("Invalid Choice!\n" +
                                                                "Choices are case sensitive!");

                                                    }

                                                } while(true);  // Get quiz type


                                                System.out.println("Enter The Quiz's Name:");
                                                String quizName = scanner.nextLine();

                                                System.out.println("Enter Question:");
                                                String question = scanner.nextLine();

                                                System.out.println("Enter Correct Answer:");
                                                String correctAns = scanner.nextLine();


                                                if (quizType.equals("MCQ")) {

                                                    System.out.println("Enter Number of Choices to Add:");
                                                    int numChoice = scanner.nextInt();
                                                    scanner.nextLine();

                                                    ArrayList<String> choices = new ArrayList<>();

                                                    for (int i = 0; i < numChoice; i++) {

                                                        System.out.printf("Enter Option %d", i + 1);
                                                        choices.add(scanner.nextLine());

                                                    }


                                                    if (quizzes.editQuiz(courseName, quizType, quizName, question,
                                                            choices, correctAns)) {

                                                        System.out.println("Successfully Added!");
                                                        Course.totalQuizzes.set(thisCourse.courseNumber,
                                                                thisCourse.numQuizzes += 1);

                                                        thisCourse.updateCourseFile();



                                                    } else {

                                                        System.out.println("Error Adding Quiz!");

                                                    }

                                                } else {

                                                    if (quizzes.editQuiz(courseName, quizType, quizName, question,
                                                            correctAns)) {

                                                        System.out.println("Successfully Added!");
                                                        Course.totalQuizzes.set(thisCourse.courseNumber,
                                                                thisCourse.numQuizzes += 1);

                                                        thisCourse.updateCourseFile();

                                                    } else {

                                                        System.out.println("Error Adding Quiz!");

                                                    }

                                                }

                                            }       // Edits a specific quiz

                                            case 5 -> {

                                                System.out.println("Enter the Name of Quiz to Remove:");
                                                String quizName = scanner.nextLine();

                                                quizzes.deleteQuiz(quizName);
                                                Course.totalQuizzes.set(thisCourse.courseNumber,
                                                        thisCourse.numQuizzes -= 1);

                                                thisCourse.updateCourseFile();

                                            }       // Removes a specific quiz

                                            default -> System.out.println("Invalid Choice!");

                                        }


                                    }

                                    case 2 -> {

                                        Course addedCourse = new Course(scanner);

                                        addedCourse.updateCourseFile();
                                        System.out.println("Course Added Successfully");

                                    }

                                    case 3 -> {}        // do nothing if user exits

                                    default -> System.out.println("Invalid Choice!");

                                }

                            } while (teacherQuizChoice != 3);

                        } else if (teacherOption != 3) {

                            System.out.println("Invalid Option!");

                        }

                    } while (teacherOption != 3);   // Teacher's menu

                } else if (thisAccount.getType().equals("student")) {

                    do {

                        System.out.println("Student's Menu \n1) Account Settings \n2) Quizzes \n3) Exit");
                        studentOption = scanner.nextInt();
                        scanner.nextLine();

                        if (studentOption == 1) {

                            System.out.println("Student Account Menu \n1) Edit Account \n2) Delete Account");
                            studentAccount = scanner.nextInt();
                            scanner.nextLine();

                            switch (studentAccount) {

                                case 1 -> {

                                    System.out.println("1) Update Username \n2) Update Password \n3) Update Name");
                                    int choice = scanner.nextInt();     // user's choice
                                    scanner.nextLine();
                                    thisAccount.updateAccount(scanner, choice);

                                }   // update account

                                case 2 -> {

                                    System.out.println("Enter 1 to confirm, anything else to cancel!");
                                    int choice = scanner.nextInt();
                                    scanner.nextLine();

                                    if (choice == 1) {

                                        thisAccount.deleteAccount();

                                    }

                                    studentOption = 3;      // sends student to main menu

                                }   // delete account

                            }

                        } else if (studentOption == 2) {

                            Course.initializeStudentGradebook();

                            do {

                                System.out.println("Student Quiz Menu");
                                System.out.println("1) Select Course\n2) View Overall Grade\n3) View Standing\n" +
                                        "4) View Class Average\n5) Exit");

                                studentQuizChoice = scanner.nextInt();
                                scanner.nextLine();

                                String quizFile = "quiz_database.txt";

                                Quiz quizzes;

                                try {
                                    quizzes = new Quiz(quizFile);
                                    Course.initializeCourses();
                                } catch (IOException e) {
                                    System.out.println("Error Reading Quiz File!");
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

                                                        System.out.println("Correct Answer!");

                                                        newGrade = ((currentStudentAttempts * currentStudentGrade
                                                        ) + 100) / (currentStudentAttempts + 1);

                                                        Course.allStudentGrades.set(studentGradebookNum, newGrade);



                                                    } else {

                                                        System.out.println("Incorrect Answer!");

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

                                                    System.out.printf("Student Attempts for %s Quizzes: %d\n",
                                                            thisCourse.courseName, totalAttempts);

                                                }

                                                case 4-> {}     // do nothing if user chooses to quit

                                                default -> System.out.println("Invalid Choice!");

                                            }

                                        } while (quizMenuChoiceStudent != 4);

                                    }

                                    case 2 -> {

                                        double studentGrade = Course.allStudentGrades.get(
                                                Course.allGradebookStudents.indexOf(thisAccount.name));
                                        System.out.printf("Your Average Grade is:\n%.2f %%\n",studentGrade);

                                    }       // Show overall grade

                                    case 3 -> {

                                        double studentGrade = Course.allStudentGrades.get(
                                                Course.allGradebookStudents.indexOf(thisAccount.name));
                                        int studentRank = Course.calculateStanding(studentGrade);
                                        int totalStudents = Course.allGradebookStudents.size();

                                        System.out.printf("You rank %d amongst %d students.\n",
                                                studentRank, totalStudents);
                                    }       // Shows student's rank

                                    case 4 -> {

                                        double averageScore = Course.calculateOverallAverage();

                                        System.out.printf("The Class Average is %.2f\n", averageScore);

                                    }       // Shows overall class average

                                    case 5 -> {}          // does nothing if user decides to exit

                                    default -> {
                                        System.out.println("Invalid Choice!");
                                    }      // invalid choice

                                }


                            } while (studentQuizChoice != 5);

                        } else if (studentOption != 3) {

                            System.out.println("Invalid Option!");

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

                     System.out.printf("Successfully created account for %s!\n", newAccount.getFullName());





            } else if (mainOption != 3){

                System.out.println("Invalid Option!");

            }

        } while (mainOption != 3);

        System.out.println("Program Closed!");

    }

}
