import java.io.*;
import java.util.ArrayList;

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

                quizzes.remove(checker);

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
            if (checker.getName().equals(name)) {

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