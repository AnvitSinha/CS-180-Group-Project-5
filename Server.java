import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {
    public static void main(String[] args) {
        try {
            QuizFile q = new QuizFile("quiz_tester.txt");
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


                //Requires quizName input from the client
                //Returns the specified quiz to client
                if (currentLine.equals("printQuiz")) {
                    String quizName = reader.readLine();
                    Quiz myQuiz = q.getQuiz(quizName);
                    writer.write(myQuiz.stringify());
                    writer.println();
                    writer.flush();
                }

                //Requires courseName and quizName input from the client
                //Creates empty quiz with quizName and courseName
                if (currentLine.equals("addQuiz")) {

                    String courseName = reader.readLine();
                    String quizName = reader.readLine();

                    writer.write(String.valueOf(q.addQuiz(courseName, quizName)));
                    writer.println();
                    writer.flush();
                }

                //Requires quizName, question type, question, answers, correctAnswer
                //Adds a new question to the specified quiz by quizName
                if (currentLine.equals("addQuestion")) {

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

                //Requires quizName
                //Deletes the specified quiz
                if (currentLine.equals("deleteQuiz")) {

                    String quizName = reader.readLine();

                    writer.write(String.valueOf(q.deleteQuiz(quizName)));
                    writer.println();
                    writer.flush();
                }

                //Requires quizName and questionNumber
                //Deletes the specified question in the specified quiz
                if (currentLine.equals("deleteQuestion")) {

                    String quizName = reader.readLine();
                    int questionNum = Integer.parseInt(reader.readLine());

                    writer.write(String.valueOf(q.deleteQuestion(quizName, questionNum)));
                    writer.println();
                    writer.flush();
                }

                //Requires quizName and newName
                //Edits the name of the specified quiz
                if (currentLine.equals("editQuizName")) {

                    String quizName = reader.readLine();
                    String newName = reader.readLine();

                    writer.write(String.valueOf(q.editQuizName(quizName, newName)));
                    writer.println();
                    writer.flush();
                }

                //Requires quizName and newCourse
                //Edits the courseName
                if (currentLine.equals("editQuizCourse")) {

                    String quizName = reader.readLine();
                    String newCourse = reader.readLine();

                    writer.write(String.valueOf(q.editQuizCourse(quizName, newCourse)));
                    writer.println();
                    writer.flush();
                }

                //Requires quizName, questionNumber, type, question, answers, correctAnswer
                //Edits the specified question
                if (currentLine.equals("editQuestion")) {

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

                //Requires courseName
                //Prints course by Course Name
                if (currentLine.equals("printCourseQuizzes")) {

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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
