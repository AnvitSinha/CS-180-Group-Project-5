import java.util.ArrayList;

public class Question {
    private int type;
    private String question;
    private ArrayList<String> answers;
    private String correctAnswer;

    //Constructor
    public Question(int type, String question, ArrayList<String> answers, String correctAnswer) {
        this.type = type;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    //Getters and Setters
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    //Helper method: turns int type to corresponding String
    public String typeToString() {
        if (type == 1) {
            return "MCQ";
        } else if (type == 0) {
            return "TF";
        } else {
            return null;
        }
    }


    //Overloaded toString method
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(typeToString());
        sb.append("\n");
        sb.append(question);
        sb.append("\n");

        for (String answer : answers) {
            sb.append(answer);
            sb.append("\n");
        }

        sb.append(correctAnswer);
        sb.append("\n");
        sb.append("questionSeparator");
        sb.append("\n");


        return sb.toString();
    }

    //Alternative toString method for server specific formatting
    public String stringify() {

        StringBuilder sb = new StringBuilder();
        sb.append(typeToString());
        sb.append("&");
        sb.append(question);
        sb.append("&");
        for (String answer : answers) {
            sb.append(answer);
            sb.append("&");
        }
        sb.append(correctAnswer);

        return sb.toString();
    }


}