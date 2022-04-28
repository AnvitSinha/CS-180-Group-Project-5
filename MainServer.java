import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainServer implements Runnable {

    private static final Object GATEKEEPER = new Object();
    private final Socket client;

    public MainServer(Socket client) {

        this.client = client;

    }

    public void run() {

        try {


            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
            String currentLine;

            Account.initializeAccounts();
            Course.initializeCourses();
            GradeBook.initializeStudentGradebook();
            QuizFile q = new QuizFile("quiz_tester.txt");

            while (true) {

                currentLine = reader.readLine();

                if (currentLine != null) {

                    switch (currentLine) {

                        // Account options
                        case "verify" -> {

                            synchronized (GATEKEEPER) {

                                String username = reader.readLine();
                                String password = reader.readLine();

                                String response = String.valueOf(Account.isValidCredential(username, password));

                                writer.println(response);
                                writer.flush();

                            }


                        }

                        case "updateAccount" -> {

                            String username = reader.readLine();
                            String password = reader.readLine();
                            Account account = new Account(username, password);

                            int choice = Integer.parseInt(reader.readLine());   // get user's choice for what to update


                            switch (choice) {

                                case 1 -> {

                                    String newUsername = reader.readLine();

                                    String response = String.valueOf(account.updateUsername(newUsername));

                                    writer.println(response);
                                    writer.flush();

                                }       // Update username

                                case 2 -> {

                                    String newPassword = reader.readLine();

                                    String response = String.valueOf(account.updatePassword(newPassword));

                                    writer.println(response);
                                    writer.flush();

                                }       // update password

                                case 3 -> {

                                    String newName = reader.readLine();

                                    account.updateName(newName);

                                    writer.println(true);
                                    writer.flush();

                                }       // update name

                            }

                        }

                        case "deleteAccount" -> {

                            String username = reader.readLine();
                            String password = reader.readLine();
                            Account account = new Account(username, password);

                            account.deleteAccount();

                            writer.println(true);
                            writer.flush();

                        }

                        case "newAccount" -> {

                            String type = reader.readLine();
                            String name = reader.readLine();
                            String username = reader.readLine();
                            String password = reader.readLine();

                            String response = String.valueOf(Account.addNewAccount(name, username, password, type));

                            writer.println(response);
                            writer.flush();


                        }

                        // Quiz Options
                        case "printQuiz" -> {
                            String quizName = reader.readLine();
                            Quiz myQuiz = q.getQuiz(quizName);
                            writer.write(myQuiz.stringify());
                            writer.println();
                            writer.flush();
                        }

                        case "addQuiz" -> {

                            String courseName = reader.readLine();
                            String quizName = reader.readLine();

                            writer.write(String.valueOf(q.addQuiz(courseName, quizName)));
                            writer.println();
                            writer.flush();
                        }

                        case "addQuestion" -> {
                            String quizName = reader.readLine();
                            int type = Integer.parseInt(reader.readLine());
                            String question = reader.readLine();
                            ArrayList<String> answers =
                                    new ArrayList<String>(Arrays.asList(reader.readLine().split("&")));
                            String correctAnswer = reader.readLine();

                            writer.write(String.valueOf(q.addQuestion(quizName, type, question, answers, correctAnswer)));
                            writer.println();
                            writer.flush();
                        }

                        case "deleteQuiz" -> {
                            String quizName = reader.readLine();

                            writer.write(String.valueOf(q.deleteQuiz(quizName)));
                            writer.println();
                            writer.flush();
                        }

                        case "deleteQuestion" -> {
                            String quizName = reader.readLine();
                            int questionNum = Integer.parseInt(reader.readLine());

                            writer.write(String.valueOf(q.deleteQuestion(quizName, questionNum)));
                            writer.println();
                            writer.flush();
                        }

                        case "editQuizName" -> {
                            String quizName = reader.readLine();
                            String newName = reader.readLine();

                            writer.write(String.valueOf(q.editQuizName(quizName, newName)));
                            writer.println();
                            writer.flush();
                        }

                        case "editQuizCourse" -> {
                            String quizName = reader.readLine();
                            String newCourse = reader.readLine();

                            writer.write(String.valueOf(q.editQuizCourse(quizName, newCourse)));
                            writer.println();
                            writer.flush();
                        }

                        case "editQuestion" -> {

                            String quizName = reader.readLine();
                            int questionNumber = Integer.parseInt(reader.readLine());
                            int type = Integer.parseInt(reader.readLine());
                            String question = reader.readLine();
                            ArrayList<String> answers =
                                    new ArrayList<String>(Arrays.asList(reader.readLine().split("&")));
                            String correctAnswer = reader.readLine();

                            writer.write(String.valueOf(q.editQuestion(quizName, questionNumber,
                                    type, question, answers, correctAnswer)));
                            writer.println();
                            writer.flush();
                        }

                        case "printCourseQuizzes" -> {
                            String courseName = reader.readLine();
                            ArrayList<Quiz> myQuiz = q.getCourseQuizzes(courseName);

                            StringBuilder sb = new StringBuilder();

                            for (Quiz temp : myQuiz) {

                                sb.append(temp.stringify());
                            }

                            writer.write(sb.toString());
                            writer.println();
                            writer.flush();
                        }

                        // Course options
                        case "addCourse" -> {

                            String courseName = reader.readLine();

                            String response = String.valueOf(Course.addCourse(courseName));

                            writer.println(response);
                            writer.flush();

                        }

                        case "listCourses" -> {

                            ArrayList<String> allCourses = Course.listCourses();

                            writer.println(allCourses.size());
                            writer.flush();

                            for (int i = 0; i < allCourses.size(); i++) {

                                writer.println(String.format("Course %d: %s",(i+1), allCourses.get(i)));
                                writer.flush();

                            }

                        }

                        //GradeBook Options
                        case "standing" -> {

                            double studentScore = Double.parseDouble(reader.readLine());

                            String response = String.valueOf(GradeBook.calculateStanding(studentScore));

                            writer.println(response);
                            writer.flush();

                        }

                        case "courseAverage" -> {

                            String course = reader.readLine();

                            String response = String.valueOf(GradeBook.calculateCourseAverage(course));

                            writer.println(response);
                            writer.flush();

                        }

                        case "studentAverage" -> {

                            String student = reader.readLine();

                            String response = String.valueOf(GradeBook.calculateStudentAverage(student));

                            writer.println(response);
                            writer.flush();

                        }

                        case "studentInCourse" -> {

                            String student = reader.readLine();
                            String course = reader.readLine();

                            String response = String.valueOf(GradeBook.studentCourseGrade(student, course));

                            writer.println(response);
                            writer.flush();

                        }

                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}