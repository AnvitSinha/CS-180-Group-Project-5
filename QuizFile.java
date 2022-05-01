import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Project 5 - QuizFile
 * <p>
 * Handles all requests for dealing with quizzes by using relevant methods and helper methods. All reading and writing
 * to databse file is done here.
 *
 * @author Group 66, L16
 * @version May 2, 2022
 */

public class QuizFile {

    private final String fileName;
    private ArrayList<Quiz> quizzes;
    private ArrayList<String> allNames;


    //Constructor
    public QuizFile(String fileName) throws Exception {
        this.fileName = fileName;
        this.quizzes = new ArrayList<>();
        readFile();
        readNames();
    }

    public ArrayList<String> getAllNames() {
        return allNames;
    }

    private void readNames() {

        allNames = new ArrayList<>();

        for (Quiz temp : quizzes) {

            allNames.add(temp.getName());
        }
    }

    public ArrayList<String> getCourseQuizName(String courseName) {

        ArrayList<String> names = new ArrayList<>();

        for (Quiz temp : quizzes) {

            if (temp.getCourse().equals(courseName)) {

                names.add(temp.getName());

            }

        }

        return names;

    }

    //Getter and Setter
    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    //Writes quizzes Arraylist to Database
    public boolean writeFile() {

        File f = new File(fileName);

        try {

            FileWriter fw = new FileWriter(f, false);
            StringBuilder sb = new StringBuilder();

            sb.append("lineQuizSeparator");
            sb.append("\n");
            sb.append(this);

            fw.write(sb.toString());
            fw.close();
            readNames();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    //Reads the Database and initializes quizzes to Arraylist
    public boolean readFile() throws Exception {

        this.quizzes = new ArrayList<>();

        File f = new File(fileName);

        if (!f.exists()) {
            return false;
        }
        ArrayList<String> temp = new ArrayList<>();

        FileReader fr = new FileReader(f);
        BufferedReader bfr = new BufferedReader(fr);

        String line;

        while ((line = bfr.readLine()) != null) {
            temp.add(line);
        }

        bfr.close();


        while (temp.size() > 1) {

            if (temp.get(0).equals("lineQuizSeparator")) {

                temp.remove(0);

                String course = temp.remove(0);
                String name = temp.remove(0);

                ArrayList<String> empty = new ArrayList<>();

                while (!temp.get(0).equals("lineQuizSeparator")) {

                    empty.add(temp.remove(0));
                }
                ArrayList<Question> questions = questionReader(empty);

                quizzes.add(new Quiz(course, name, questions));
            }
        }

        return true;
    }

    // Adds quiz file from teacher to database
    public boolean addQuizFile(String fileToAdd) throws Exception {

        File f = new File(fileToAdd);

        if (!f.exists()) {
            return false;
        }

        BufferedReader br = new BufferedReader(new FileReader(f));

        ArrayList<String> temp = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            temp.add(line);
        }
        br.close();

        int courseNum;

        while (temp.size() > 1) {

            if (temp.get(0).equals("lineQuizSeparator")) {

                temp.remove(0);

                String course = temp.remove(0);
                String name = temp.remove(0);

                courseNum = Course.allCourses.indexOf(course);

                ArrayList<String> empty = new ArrayList<>();

                while (!temp.get(0).equals("lineQuizSeparator")) {

                    empty.add(temp.remove(0));
                }
                ArrayList<Question> questions = questionReader(empty);

                quizzes.add(new Quiz(course, name, questions));

                Course.totalQuizzes.set(courseNum, Course.totalQuizzes.get(courseNum) + 1);


            }
        }

        Course.updateCourseFile();

        writeFile();

        return true;


    }

    //Helper Method for readFile method
    public ArrayList<Question> questionReader(ArrayList<String> input) {

        ArrayList<Question> questions = new ArrayList<>();

        while (input.size() > 1) {

            if (input.get(0).equals("questionSeparator")) {
                input.remove(0);

                String stringType = input.remove(0);
                int type = 0;
                if (stringType.equals("MCQ")) {
                    type = 1;
                }
                String question = input.remove(0);

                ArrayList<String> answers = new ArrayList<>();

                while (!input.get(0).equals("questionSeparator")) {


                    answers.add(input.remove(0));
                }

                String correctAnswer = answers.remove(answers.size() - 1);
                questions.add(new Question(type, question, answers, correctAnswer));
            }
        }

        return questions;
    }

    //Adds new quiz to Quizzes Arraylist calls writeFile()
    public boolean addQuiz(String course, String name) {


        for (Quiz checker : quizzes) {
            if (checker.getName().equals(name)) {
                return false;
            }
        }


        Quiz q = new Quiz(course, name, new ArrayList<>());
        quizzes.add(q);

        int courseNum = Course.allCourses.indexOf(course);

        Course.totalQuizzes.set(courseNum, Course.totalQuizzes.get(courseNum) + 1);
        Course.updateCourseFile();

        writeFile();

        return true;
    }

    //Adds a question to the specified Quiz
    public boolean addQuestion(String name, int type, String question,
                               ArrayList<String> answers, String correctAnswer) {

        ArrayList<String> realAnswers = answers;

        if (type == 0) {
            realAnswers = new ArrayList<>();
            realAnswers.add("True");
            realAnswers.add("False");

        }

        for (Quiz checker : quizzes) {
            if (checker.getName().equals(name)) {

                checker.getQuestions().add(new Question(type, question, realAnswers, correctAnswer));
                writeFile();
                return true;
            }
        }


        return false;
    }

    //Deletes the specified Quiz including questions
    public boolean deleteQuiz(String name) {
        for (Quiz checker : quizzes) {
            if (checker.getName().equals(name)) {

                int courseNum = Course.allCourses.indexOf(checker.getCourse());

                quizzes.remove(checker);


                Course.totalQuizzes.set(courseNum, Course.totalQuizzes.get(courseNum) - 1);
                Course.updateCourseFile();

                writeFile();

                return true;
            }
        }
        return false;
    }

    //Deletes the specified question in the specified Quiz
    public boolean deleteQuestion(String quizName, int questionNumber) {

        for (Quiz checker : quizzes) {
            if (checker.getName().equals(quizName)) {

                if (checker.getQuestions().size() < questionNumber || questionNumber <= 0) {
                    return false;
                }

                checker.getQuestions().remove(questionNumber - 1);
                writeFile();

                return true;
            }
        }
        return false;
    }

    //Gets the answers for the quiz questions
    public ArrayList<String> getQuizAnswers(String quizName, ArrayList<String> questionNames) {

        Quiz check = getQuiz(quizName);
        String[] answer = new String[questionNames.size()];

        for (Question question : check.getQuestions()) {

            if (questionNames.contains(question.getQuestion())) {

                // Put correct answer at the same index as its question in the input ArrayList
                answer[questionNames.indexOf(question.getQuestion())] = question.getCorrectAnswer();

            }

        }

        return new ArrayList<>(Arrays.asList(answer));

    }

    //Gets the number of questions answered correctly by the student given a .txt file
    public int numCorrectAns(String filename) throws IOException {

        int numCorrect = 0;

        BufferedReader br = new BufferedReader(new FileReader(filename));

        String quizName = br.readLine();
        ArrayList<String> questionsGiven = new ArrayList<>();
        ArrayList<String> answersGiven = new ArrayList<>();

        String line;

        while ((line = br.readLine()) != null) {    // Add all questions and answers to the respective AL

            questionsGiven.add(line);
            answersGiven.add(br.readLine());

        }

        ArrayList<String> correctAns = getQuizAnswers(quizName, questionsGiven);

        for (int i = 0; i < answersGiven.size(); i++) {

            if (answersGiven.get(i).equalsIgnoreCase(correctAns.get(i))) {

                numCorrect++;

            }

        }

        return numCorrect;


    }

    //Randomize questions ans answer choices
    public Quiz randomizeQuestions(String quizName) {

        Quiz temp = getQuiz(quizName);

        ArrayList<Question> tempRandom = temp.getQuestions();
        Collections.shuffle(tempRandom);    // Shuffle the questions

        ArrayList<Question> randomized = new ArrayList<>();

        for (Question check : tempRandom) {

            Collections.shuffle(check.getAnswers());    //Shuffle options

        }

        if (tempRandom.size() < 5) {    // if there are less than 5 questions

            randomized = tempRandom;

            return new Quiz(temp.getCourse(), quizName, randomized);

        }

        for (int i = 0; i < 5; i++) {   // first 5 questions of the randomized list

            randomized.add(tempRandom.get(i));

        }

        return new Quiz(temp.getCourse(), quizName, randomized);

    }

    //Edits the name of the Quiz
    public boolean editQuizName(String name, String newName) {

        for (Quiz checker : quizzes) {
            if (checker.getName().equals(newName)) {
                return false;
            }
        }

        for (Quiz checker : quizzes) {
            if (checker.getName().equals(name)) {

                checker.setName(newName);

                writeFile();

                return true;
            }
        }
        return false;

    }

    //Edits the Course of the Quiz
    public boolean editQuizCourse(String name, String newCourse) {

        for (Quiz checker : quizzes) {
            if (checker.getName().equals(name)) {

                checker.setCourse(newCourse);


                writeFile();

                return true;
            }
        }
        return false;
    }


    //Edits the question based off of the specified quizName and questionNumber
    public boolean editQuestion(String quizName, int questionNumber, int type, String question,
                                ArrayList<String> possibleAnswers, String correctAnswer) {
        for (Quiz checker : quizzes) {
            if (checker.getName().equals(quizName)) {

                if (checker.getQuestions().size() < questionNumber || questionNumber <= 0) {
                    return false;
                }

                checker.getQuestions().get(questionNumber - 1).setType(type);
                checker.getQuestions().get(questionNumber - 1).setQuestion(question);
                checker.getQuestions().get(questionNumber - 1).setAnswers(possibleAnswers);
                checker.getQuestions().get(questionNumber - 1).setCorrectAnswer(correctAnswer);
                writeFile();

                return true;
            }
        }
        return false;
    }


    //gets the specific quiz based on the name provided
    public Quiz getQuiz(String name) {

        for (Quiz checker : quizzes) {
            if (checker.getName().equalsIgnoreCase(name)) {

                return checker;


            }
        }

        return null;
    }

    //gets all quizzes with the specified courseName
    public ArrayList<Quiz> getCourseQuizzes(String courseName) {

        ArrayList<Quiz> qui = new ArrayList<>();
        for (Quiz checker : quizzes) {
            if (checker.getCourse().equals(courseName)) {

                qui.add(checker);
            }
        }
        return qui;
    }

    //Debugging method
    public void printQuiz(String name) {

        for (Quiz checker : quizzes) {
            if (checker.getName().equals(name)) {

                System.out.println(checker);

            }
        }
    }

    //Debugging method
    public void printCourseQuizzes(String courseName) {

        for (Quiz checker : quizzes) {
            if (checker.getCourse().equals(courseName)) {

                System.out.println(checker);
            }
        }
    }


    //Debugging Method
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Quiz quiz : quizzes) {
            sb.append(quiz.toString());
        }

        return sb.toString();
    }
}