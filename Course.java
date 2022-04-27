import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
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

    public String courseName;           // Name
    public int numQuizzes;      // Number of Quizzes that the course has
    public int courseNumber;    // Index of the course in the arraylist
    public int courseAttempts;  // attempts for this course
    public ArrayList<Quiz> courseQuizzes;  // Quizzes for this course

    public static ArrayList<String> allCourses = new ArrayList<>();
    public static ArrayList<Integer> totalAttempts = new ArrayList<>();         // total quiz attempts for each course



    private static String coursesFile = "courses.txt";


    public Course(String courseName, QuizFile file) {

        this.courseNumber = allCourses.indexOf(courseName);
        this.courseName = courseName;
        this.courseQuizzes = file.getCourseQuizzes(courseName);
        this.numQuizzes = this.courseQuizzes.size();
        this.courseAttempts = totalAttempts.get(courseNumber);


    }   // Constructor

    public Course(Scanner scanner) {

        String newCourse;

        do {

            System.out.println("Enter the new course's name:");
            newCourse = scanner.nextLine();

            if (allCourses.contains(newCourse)) {

                System.out.println("Course already exists!");

            }

        } while(allCourses.contains(newCourse));

        allCourses.add(newCourse);
        totalAttempts.add(0);

    }

    public static void listCourses() {

        for(int i = 1; i <= allCourses.size(); i++) {

            System.out.printf("Course %d: %s\n", i, allCourses.get(i-1));

        }

    }   // Lists all courses

    public static void initializeCourses() throws IOException {

        BufferedReader bfr = new BufferedReader(new FileReader(coursesFile));

        String line;

        while((line = bfr.readLine()) != null) {

            String[] splitWords = line.split(",");

            allCourses.add(splitWords[0]);
            totalAttempts.add(Integer.valueOf(splitWords[2]));


        }

    }       // Initialize arraylists from database


    public int getTotalAttempts() {

        return this.courseAttempts;

    }       // returns total attempts for this quiz

    public int getNumQuizzes() {

        return this.numQuizzes;

    }          // returns total quizzes in this course

    public void updateCourseFile() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(coursesFile, false))) {

            for (int i = 0; i < allCourses.size(); i++) {

                String toWrite = String.format("%s,%d", allCourses.get(i), totalAttempts.get(i));

                pw.println(toWrite);
            }

        } catch (IOException e) {
            System.out.println("An Error Occurred!");;
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
        return String.format("Course Name: %s\nCourse Number: %d\nTotal Quizzes: %d\nTotal Attempts: %d",
                this.courseName, (this.courseNumber + 1), this.numQuizzes, this.courseAttempts);
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
//    public void randomizeQuestion() {
//        Collections.shuffle(courseQuizzes);
//        ArrayList<String> tmp = new ArrayList<String>();
//
//    }
}