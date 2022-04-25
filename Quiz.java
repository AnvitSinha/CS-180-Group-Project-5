import java.util.ArrayList;

public class Quiz {
    private String course;
    private String name;
    private ArrayList<Question> questions;

    //Constructor
    public Quiz(String course, String name, ArrayList<Question> questions) {
        this.course = course;
        this.name = name;
        this.questions = questions;
    }

    //Getters and Setters
    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }


    //Overloaded toString method
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(course);
        sb.append("\n");
        sb.append(name);
        sb.append("\n");
        sb.append("questionSeparator");
        sb.append("\n");
        for (Question question : questions) {

            sb.append(question.toString());

        }

        sb.append("lineQuizSeparator");
        sb.append("\n");

        return sb.toString();
    }

    //Alternative toString method for server specific formatting
    public String stringify() {

        StringBuilder sb = new StringBuilder();
        sb.append(course);
        sb.append("&");
        sb.append(name);
        sb.append("&");
        for (Question question : questions) {
            sb.append(question.stringify());
        }

        return sb.toString();
    }


}

