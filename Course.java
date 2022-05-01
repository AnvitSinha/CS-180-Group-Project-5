import java.io.*;
import java.util.ArrayList;

/**
 * Project 5 - Course
 * <p>
 * Class that manages all courses by storing them into a database and reading from the database when needed.
 * The class also includes helper methods that help in adding, deleting, listing or editing courses.
 *
 * @author Group 66, L16
 * @version May 2, 2022
 */

public class Course {

    private static final String coursesFile = "courses.txt";
    public static ArrayList<String> allCourses = new ArrayList<>();
    public static ArrayList<Integer> totalQuizzes = new ArrayList<>();   // total quizzes for each course
    public String courseName;   // Name
    public int numQuizzes;      // Number of Quizzes that the course has
    public int courseNumber;    // Index of the course in the arraylist
    public ArrayList<Quiz> courseQuizzes;  // Quizzes for this course


    public Course(String courseName, QuizFile file) {

        this.courseNumber = allCourses.indexOf(courseName);
        this.courseName = courseName;
        this.courseQuizzes = file.getCourseQuizzes(courseName);
        this.numQuizzes = this.courseQuizzes.size();

    }       // Constructor

    public static boolean addCourse(String courseName) {

        if (allCourses.contains(courseName)) {  // If course already exists

            return false;

        }

        allCourses.add(courseName);
        totalQuizzes.add(0);

        return Course.updateCourseFile();

    }

    public static boolean deleteCourse(String courseName) {

        if (!allCourses.contains(courseName)) {

            return false;

        }

        totalQuizzes.remove(allCourses.indexOf(courseName));
        allCourses.remove(courseName);

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

        while ((line = bfr.readLine()) != null) {

            String[] splitWords = line.split(",");

            allCourses.add(splitWords[0]);
            totalQuizzes.add(Integer.valueOf(splitWords[1]));


        }

    }       // Initialize arraylists from database

    public static boolean updateCourseFile() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(coursesFile, false))) {

            for (int i = 0; i < allCourses.size(); i++) {

                String toWrite = String.format("%s,%d", allCourses.get(i), totalQuizzes.get(i));

                pw.println(toWrite);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }       // Update course file with new course details

    public int getNumQuizzes() {

        return this.numQuizzes;

    }          // returns total quizzes in this course

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


}