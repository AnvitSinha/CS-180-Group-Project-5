import java.io.*;
import java.util.ArrayList;

/**
 * Project 4 - Quiz
 * <p>
 * This class implements all the functionality needed by a quiz by methods that print quizzes, add quizzes and delete
 * quizzes. This class also includes helper methods that read from the quiz datbase and initialize all the static fields
 *
 * @author Group 66, L16
 * @version April 11, 2022
 */

public class Quiz {

    public static String quizFile;
    public static ArrayList<ArrayList<String>> quizList;

    public Quiz(String fileName) throws IOException {
        Quiz.quizFile = fileName;
        quizList = new ArrayList<ArrayList<String>>();
        readQuizFile();
    }


    //Checks if the quiz already exists with the same name
    public boolean checkIfExists(String quizName) {
        for (ArrayList<String> strings : quizList) {
            if (strings.get(0).equals(quizName)) {
                return true;
            }
        }
        return false;
    }


    //reads quiz_database.txt and adds quiz Blocks to to quizList
    public static void readQuizFile() throws IOException {
        File f = new File(quizFile);
        ArrayList<String> temp = new ArrayList<>();

        FileReader fr = new FileReader(f);
        BufferedReader bfr = new BufferedReader(fr);

        String line;

        while ((line = bfr.readLine()) != null) {
            temp.add(line);
        }

        bfr.close();

        ArrayList<String> temp2 = new ArrayList<>();


        for (String s : temp) {
            if (!s.equals("lineQuizSeparator")) {
                temp2.add(s);
            } else {
                quizList.add(temp2);
                temp2 = new ArrayList<>();

            }
        }

    }

    public static void updateQuizFile(String filename, Course course, int questionsAdded) {

        ArrayList<String> allLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = br.readLine()) != null) {

                allLines.add(line);

            }

        } catch (IOException e) {
            System.out.println("Error Reading Quiz File!");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(quizFile, true))) {

            for (String s : allLines) {

                pw.println();
                pw.write(s);

            }

        } catch (IOException e) {
            System.out.println("An Error Occurred!");
        }

        course.numQuizzes += questionsAdded;
        Course.totalQuizzes.set(course.courseNumber, course.numQuizzes);

        try {
            readQuizFile();
            course.updateCourseFile();
        } catch (IOException e) {
            System.out.println("Error Updating Quiz File!");
        }

    }

    //prints the contents of quizList
    public void printQuizzes(String type) {

        for (ArrayList<String> quizzes : quizList) {
            formatter(quizzes, type);
        }
    }

    //prints specific quiz based on quizNumber
    public void printQuiz(String quizName, String type) {
        for (ArrayList<String> strings : quizList) {
            if (strings.get(2).equals(quizName)) {
                formatter(strings, type);
            }
        }

    }

    // Print quizzes for a specific course
    public void printCourseQuizzes(Course courseName) {

        for (int i = 0; i < courseName.courseQuizzes.size(); i++) {

            System.out.printf("Quiz %d: %s\n", (i+1), courseName.courseQuizzes.get(i).get(2));

        }

    }

    //formats print quiz and printQuizzes
    private void formatter(ArrayList<String> list, String type) {
        System.out.println("Course: " + list.get(0));
        System.out.println("Type: " + list.get(1));
        System.out.println("Quiz: " + list.get(2));
        System.out.println("Question: " + list.get(3));
        for (int i = 4; i < list.size() - 1; i++) {
            System.out.println("Choice #" + (i - 3) + ": " + list.get(i));
        }
        if (type.equals("teacher")) {
            System.out.println("Answer: " + list.get(list.size() - 1));
        }
        System.out.println();
    }

    //adds a quiz to the quiz_database.txt
    //Overloaded methods
    public boolean addQuiz(String course, String type, String quizName,
                           String question, ArrayList<String> choices, String correctAnswer) {
        if (checkIfExists(quizName))
            return false;
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(course);
        sb.append("\n");
        sb.append(type);
        sb.append("\n");
        sb.append(quizName);
        sb.append("\n");
        sb.append(question);
        sb.append("\n");

        for (String choice : choices) {
            sb.append(choice);
            sb.append("\n");
        }

        sb.append(correctAnswer);
        sb.append("\n");
        sb.append("lineQuizSeparator");

        File f = new File(quizFile);
        try {

            FileWriter fw = new FileWriter(f,true);
            fw.write(sb.toString());

            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }   // MCQ

    public boolean addQuiz(String course, String type, String quizName,
                           String question, String correctAnswer) {
        if (checkIfExists(quizName))
            return false;
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(course);
        sb.append("\n");
        sb.append(type);
        sb.append("\n");
        sb.append(quizName);
        sb.append("\n");
        sb.append(question);
        sb.append("\n");

        sb.append("T");
        sb.append("\n");
        sb.append("F");
        sb.append("\n");

        sb.append(correctAnswer);
        sb.append("\n");
        sb.append("lineQuizSeparator");

        File f = new File(quizFile);
        try {

            FileWriter fw = new FileWriter(f,true);
            fw.write(sb.toString());

            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }   // True/False

    //Edits a Quiz
    public boolean editQuiz(String course,String type, String quizName,
                            String question, ArrayList<String> choices, String correctAnswer) {

        for (ArrayList<String> quizzes : quizList) {

            if (quizzes.get(2).equals(quizName)) {
                quizzes.set(0, course);
                quizzes.set(3, question);
                int counter = 0;
                for (int j = 4; j < quizzes.size() - 1; j++) {
                    quizzes.set(j, choices.get(counter));
                    counter++;
                }
                quizzes.set(quizzes.size() - 1, correctAnswer);

                writeQuizList();
                return true;
            }

        }

        return false;
    }   // Edit MCQ

    public boolean editQuiz(String course,String type,
                            String quizName, String question, String correctAnswer) {
        for (ArrayList<String> quizzes : quizList) {
            if (quizzes.get(1).equalsIgnoreCase("TF")) {
                if (quizzes.get(2).equals(quizName)) {
                    quizzes.set(0, course);
                    quizzes.set(3, question);

                    quizzes.set(quizzes.size() - 1, correctAnswer);

                    writeQuizList();
                    return true;
                }
            }

        }

        return false;
    }   // Edit True/False

    //deletes specific quiz based on quizNumber/title
    public boolean deleteQuiz(String quizName) {
        for (int i = 0; i < quizList.size(); i++) {
            if (quizList.get(i).get(0).equals(quizName)) {
                quizList.remove(i);
                return writeQuizList();
            }
        }
        return false;
    }

    //updates quiz_database.txt with new quiz list also a helper function
    public boolean writeQuizList() {
        File f = new File(quizFile);

        try {

            FileWriter fw = new FileWriter(f, false);
            StringBuilder sb = new StringBuilder();

            for (ArrayList<String> quizzes : quizList) {
                for (String quiz : quizzes) {
                    sb.append(quiz);
                    sb.append("\n");
                }
                sb.append("lineQuizSeparator\n");
            }
            fw.write(sb.toString());
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //deletes full quiz file
    public boolean deleteQuizFile() {
        File f = new File(quizFile);

        return f.delete();
    }


}
