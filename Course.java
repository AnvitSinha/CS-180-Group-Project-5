import java.io.*;
import java.util.ArrayList;
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
    public ArrayList<ArrayList<String>> courseQuizzes = new ArrayList<>();  // Quizzes for this course

    public static ArrayList<String> allCourses = new ArrayList<>();
    public static ArrayList<Integer> totalQuizzes = new ArrayList<>();          // total quizzes for each course
    public static ArrayList<Integer> totalAttempts = new ArrayList<>();         // total quiz attempts for each course

    public static ArrayList<String> allGradebookStudents = new ArrayList<>();      // Names of students in the gradebook
    public static ArrayList<Integer> allStudentAttempts = new ArrayList<>();    // Attempts for each student
    public static ArrayList<Double> allStudentGrades = new ArrayList<>();      // Grades of each student

    private static String coursesFile = "courses.txt";
    private static String studentGradebook = "student_grades.txt";

    public Course(String courseName) {

        this.courseNumber = allCourses.indexOf(courseName);
        this.courseName = courseName;
        this.numQuizzes = totalQuizzes.get(courseNumber);
        this.courseAttempts = totalAttempts.get(courseNumber);

        for (ArrayList<String> strings : Quiz.quizList) {
            if (strings.get(0).equals(courseName)) {
                courseQuizzes.add(strings);
            }
        }

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
        totalQuizzes.add(0);
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
            totalQuizzes.add(Integer.valueOf(splitWords[1]));
            totalAttempts.add(Integer.valueOf(splitWords[2]));


        }

    }       // Initialize arraylists from database

    public static void initializeStudentGradebook() {

        try (BufferedReader bfr = new BufferedReader(new FileReader(studentGradebook))) {

            String line;

            while((line = bfr.readLine()) != null) {

                String[] splitWords = line.split(",");

                allGradebookStudents.add(splitWords[0]);
                allStudentAttempts.add(Integer.valueOf(splitWords[1]));
                allStudentGrades.add(Double.valueOf(splitWords[2]));


            }

        } catch (IOException e) {

            System.out.println("Error Reading Student Gradebook!");

        }

    }

    public static void updateStudentGradebook() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(studentGradebook, false))) {

            for (int i = 0; i < allGradebookStudents.size(); i++) {

                pw.println(String.format("%s,%d,%.2f",
                        allGradebookStudents.get(i), allStudentAttempts.get(i), allStudentGrades.get(i)));

            }

        } catch (IOException e) {

            System.out.println("Error updating grade book!");
            System.out.println("Please contact your teacher!");

        }

    }

    public static int calculateStanding(double score) {

        int position = 1;

        for (Double i : allStudentGrades) {

            if (score < i) {

                position += 1;

            }

        }

        return position;

    }

    public static double calculateOverallAverage() {

        double sum = 0;

        for (double i : allStudentGrades) {

            sum += i;

        }

        return (sum / allStudentGrades.size());

    }

    public static void addNewStudent(String name) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(studentGradebook, true))) {

            pw.println(String.format("%s,0,0", name));      // Add student with 0 quizzes taken and 0 as average

        } catch (IOException e) {

            System.out.println("Error Adding Student to Gradebook!");

        }

    }

    public ArrayList<ArrayList<String>> getCourseQuizzes() {

        ArrayList<ArrayList<String>> courseQuiz = new ArrayList<>();

        for (ArrayList<String > quiz : Quiz.quizList) {

            if(quiz.get(0).equals(this.courseName)) {

                courseQuiz.add(quiz);

            }

        }

        return courseQuiz;

    }

    public int getTotalAttempts() {

        return this.courseAttempts;

    }       // returns total attempts for this quiz

    public int getNumQuizzes() {

        return this.numQuizzes;

    }          // returns total quizzes in this course

    public void updateCourseFile() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(coursesFile, false))) {

            for (int i = 0; i < allCourses.size(); i++) {

                String toWrite = String.format("%s,%d,%d", allCourses.get(i), totalQuizzes.get(i),
                        totalAttempts.get(i));

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

}