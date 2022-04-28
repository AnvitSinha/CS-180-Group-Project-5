import javax.xml.stream.FactoryConfigurationError;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Project 4 - Course
 * <p>
 * Class that manages all courses by storing them into a database and reading from the database when needed.
 * The class also includes helper methods that help in adding courses, initializing courses, and also holds the
 * grade book for the students.
 * Teh grade book hold the name, number of attempted quizzes, and average score for that student.
 *
 * @author Group 66, L16
 * @version April 11, 2022
 */

public class Course {

    public String courseName;   // Name
    public int numQuizzes;      // Number of Quizzes that the course has
    public int courseNumber;    // Index of the course in the arraylist
    public ArrayList<Quiz> courseQuizzes;  // Quizzes for this course

    public static ArrayList<String> allCourses = new ArrayList<>();
    public static ArrayList<Integer> totalQuizzes = new ArrayList<>();   // total quizzes for each course

    private static final String coursesFile = "courses.txt";


    public Course(String courseName, QuizFile file) {

        this.courseNumber = allCourses.indexOf(courseName);
        this.courseName = courseName;
        this.courseQuizzes = file.getCourseQuizzes(courseName);
        this.numQuizzes = this.courseQuizzes.size();

        allCourses.add(courseName);



    }   // Constructor

    public static boolean addCourse(String courseName) {

        if (allCourses.contains(courseName)) {

            return false;

        }

        allCourses.add(courseName);
        totalQuizzes.add(0);

        return Course.updateCourseFile();

    }

    public static ArrayList<String> listCourses() {

        return allCourses;

    }   // Lists all courses

    public static void initializeCourses() throws IOException {

        allCourses = new ArrayList<>();
        totalQuizzes = new ArrayList<>();

        BufferedReader bfr = new BufferedReader(new FileReader(coursesFile));

        String line;

        while((line = bfr.readLine()) != null) {

            String[] splitWords = line.split(",");

            allCourses.add(splitWords[0]);
            totalQuizzes.add(Integer.valueOf(splitWords[1]));


        }

    }       // Initialize arraylists from database

    public int getNumQuizzes() {

        return this.numQuizzes;

    }          // returns total quizzes in this course

    public static boolean updateCourseFile() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(coursesFile, false))) {

            for (int i = 0; i < allCourses.size(); i++) {

                String toWrite = String.format("%s,%d", allCourses.get(i), totalQuizzes.get(i));

                pw.println(toWrite);
            }

            return true;

        } catch (IOException e) {
            return false;
        }

    }       // Update course file with new course details

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return String.format("Course Name: %s\nCourse Number: %d\nTotal Quizzes: %d",
                this.courseName, (this.courseNumber + 1), this.numQuizzes);
    }

    //attach a file
    public String attachFile(String filename) throws IOException {
        ArrayList<String> temp = new ArrayList<>();
        File f = new File(filename);
        FileReader fr = new FileReader(f);
        BufferedReader bfr = new BufferedReader(fr);
        String line;

        while ((line = bfr.readLine()) != null) {
            temp.add(line);
        }

        return temp.toString();
    }

    //randomize the order of questions
    public ArrayList<Quiz> randomizeQuestion() {

        ArrayList<Quiz> assigned = courseQuizzes;
        Collections.shuffle(assigned);

        return assigned;

    }

}