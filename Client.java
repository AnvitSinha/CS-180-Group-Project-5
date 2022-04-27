import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws IOException {


        Socket socket = new Socket("localhost", 6969);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        //printQuiz
        writer.write("printQuiz");
        writer.println();
        writer.flush();

        writer.write("Midterm 5");
        writer.println();
        writer.flush();


        String response = reader.readLine();
        System.out.println(quizFormatter(response));

        //addQuiz
        writer.write("addQuiz");
        writer.println();
        writer.flush();

        writer.write("CS 180");
        writer.println();
        writer.flush();

        writer.write("Final Exam");
        writer.println();
        writer.flush();

        response = reader.readLine();
        System.out.println("Adding quiz " + response);

        //addQuestion
        writer.write("addQuestion");
        writer.println();
        writer.flush();

        writer.write("Final Exam");
        writer.println();
        writer.flush();

        writer.write("1");
        writer.println();
        writer.flush();

        writer.write("Is the Earth round?");
        writer.println();
        writer.flush();

        //Simulating user adding answers
        ArrayList<String> answers = new ArrayList<>();
        answers.add("answer 1");
        answers.add("answer 2");
        answers.add("answer 3");
        answers.add("answer 4");

        writer.write(answersFormatter(answers));
        writer.println();
        writer.flush();

        writer.write("answer 4");
        writer.println();
        writer.flush();

        response = reader.readLine();
        System.out.println("Adding question: " + response);

        //deleteQuiz
//        writer.write("deleteQuiz");
//        writer.println();
//        writer.flush();
//
//        writer.write("Final Exam");
//        writer.println();
//        writer.flush();
//
//        response = reader.readLine();
//        System.out.println("Deleted quiz: " + response);

        //deleteQuestion
//        writer.write("deleteQuestion");
//        writer.println();
//        writer.flush();
//
//        writer.write("Final Exam");
//        writer.println();
//        writer.flush();
//
//        writer.write("1");
//        writer.println();
//        writer.flush();
//
//        response = reader.readLine();
//        System.out.println("Deleting question: " + response);

        //editQuizName
        writer.write("editQuizName");
        writer.println();
        writer.flush();

        writer.write("Final Exam");
        writer.println();
        writer.flush();

        writer.write("Midterm 6");
        writer.println();
        writer.flush();

        response = reader.readLine();
        System.out.println("Edit quizName: " + response);

        //editQuizCourse

        writer.write("editQuizCourse");
        writer.println();
        writer.flush();

        writer.write("Midterm 6");
        writer.println();
        writer.flush();

        writer.write("MA 161");
        writer.println();
        writer.flush();

        response = reader.readLine();
        System.out.println("Edit quizCourse: " + response);

        //editQuestion
        writer.write("editQuestion");
        writer.println();
        writer.flush();

        writer.write("Final Exam");
        writer.println();
        writer.flush();

        writer.write("1");
        writer.println();
        writer.flush();

        writer.write("1");
        writer.println();
        writer.flush();

        writer.write("Is the Earth round?");
        writer.println();
        writer.flush();

        //Simulating user adding answers
        answers = new ArrayList<>();
        answers.add("answer 1");
        answers.add("answer 2");
        answers.add("answer 3");
        answers.add("answer 4");

        writer.write(answersFormatter(answers));
        writer.println();
        writer.flush();

        writer.write("answer 4");
        writer.println();
        writer.flush();

        response = reader.readLine();
        System.out.println("Editing Question: " + response);

        //printCourseQuizzes
        writer.write("printCourseQuizzes");
        writer.println();
        writer.flush();

        writer.write("MA 161");
        writer.println();
        writer.flush();


        response = reader.readLine();
        System.out.println(quizFormatter(response));



        writer.close();
        reader.close();
    }

    //TODO adapt to GUI, might need to return Arraylist instead of String @Anvit
    //Formats specific server output
    private static String quizFormatter(String input) {

        ArrayList<String> container = new ArrayList<String>(Arrays.asList(input.split("&")));

        StringBuilder sb = new StringBuilder();

        for (String temp : container) {

            sb.append(temp);
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
}