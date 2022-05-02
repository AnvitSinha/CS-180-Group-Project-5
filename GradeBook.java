import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Project 5 - GradeBook
 * <p>
 * Handles all requests for the student grade book. Has all methods to calculate averages for quizzes, courses, and
 * students, as well as methods for adding submissions with time stamps.
 *
 * @author Group 66, L16
 * @version May 2, 2022
 */

public class GradeBook {

    private static final String studentGradebook = "GradeBook.txt";
    public static ArrayList<String> allGradebookStudents = new ArrayList<>();   // Names of students in the grade book
    public static ArrayList<String> allQuizNames = new ArrayList<>();           // Name for each quiz taken in order
    public static ArrayList<Double> allStudentScores = new ArrayList<>();       // Score for each quiz in order
    public static ArrayList<String> allCourseName = new ArrayList<>();          // Name of all associated courses
    public static ArrayList<String> allSubmissionTimes = new ArrayList<>();     // All Submission times in order

    public static void initializeStudentGradebook() throws IOException {

        allGradebookStudents = new ArrayList<>();
        allQuizNames = new ArrayList<>();
        allStudentScores = new ArrayList<>();
        allCourseName = new ArrayList<>();
        allStudentScores = new ArrayList<>();

        BufferedReader bfr = new BufferedReader(new FileReader(studentGradebook));

        String line;

        while((line = bfr.readLine()) != null) {

            String[] splitWords = line.split(",");

            allGradebookStudents.add(splitWords[0]);
            allQuizNames.add(splitWords[1]);
            allStudentScores.add(Double.parseDouble(splitWords[2]));
            allCourseName.add(splitWords[3]);
            allSubmissionTimes.add(splitWords[4]);

        }


    }   // Initialize from databse

    public static boolean updateStudentGradebook() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(studentGradebook, false))) {

            for (int i = 0; i < allGradebookStudents.size(); i++) {

                pw.println(String.format("%s,%s,%.2f,%s,%s", allGradebookStudents.get(i),
                        allQuizNames.get(i), allStudentScores.get(i), allCourseName.get(i), allSubmissionTimes.get(i)));

            }

            return true;

        } catch (IOException e) {

            return false;

        }

    }

    public static double calculateCourseAverage(String courseName) {

        double sum = 0;
        int num = 0;

        for (int i = 0; i < allCourseName.size(); i++) {

            if (allCourseName.get(i).equals(courseName)) {      // If the course at the index is the one to check

                sum += allStudentScores.get(i);     // Add the score in that position to the total sum
                num++;

            }

        }

        if (num == 0) {

            return 0;

        }

        return (sum / num);

    } //Finds the average in that course for all students

    public static double calculateStudentAverage(String studentName) {

        double sum = 0;
        int num = 0;

        for (int i = 0; i < allGradebookStudents.size(); i++) {

            if (allGradebookStudents.get(i).equals(studentName)) {      // If the course at the index is the one to check

                sum += allStudentScores.get(i);     // Add the score in that position to the total sum
                num++;

            }

        }

        if (num == 0) {

            return 0;

        }

        return (sum / num);

    }   //Find student's overall average

    public static double studentCourseGrade(String studentName, String courseName) {

        double sum = 0;
        int num = 0;

        for (int i = 0; i < allGradebookStudents.size(); i++) {

            if (allGradebookStudents.get(i).equals(studentName)) {

                if (allCourseName.get(i).equals(courseName)) {

                    sum += allStudentScores.get(i);
                    num++;

                }

            }

        }

        if (num == 0) {

            return 0.00;

        }

        return (sum / num);

    }   //Find student's average in course

    public static boolean addNewSubmission(String studentName, String quizName, double score, String courseName) {

        allGradebookStudents.add(studentName);
        allQuizNames.add(quizName);
        allStudentScores.add(score);
        allCourseName.add(courseName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        ZonedDateTime now = ZonedDateTime.now();

        String submissionTime = now.format(formatter);

        allSubmissionTimes.add(submissionTime);

        try (PrintWriter pw = new PrintWriter(new FileWriter(studentGradebook, true))) {

            pw.println(String.format("%s,%s,%.2f,%s,%s", studentName, quizName, score, courseName, submissionTime));

            return true;

        } catch (IOException e) {

            return false;

        }

    }

    public static double calculateQuizAverage(String quizName) {

        double sum = 0;
        int num = 0;

        for (int i = 0; i < allQuizNames.size(); i++) {

            if (allQuizNames.get(i).equals(quizName)) { // if quiz name is same as provided input

                sum += allStudentScores.get(i);
                num++;

            }

        }

        if (num == 0) {

            return 0;

        }

        return (sum / num);

    }   // Find average Student score for quiz

    public static ArrayList<String> viewQuizSubmissions(String quizName) {

        ArrayList<String> quizSubmissions = new ArrayList<>();

        for(int i = 0; i < allQuizNames.size(); i++) {

            if (allQuizNames.get(i).equalsIgnoreCase(quizName)) {

                String submission = String.format("Student: %s\nScore: %.2f\nSubmitted at: %s",
                        allGradebookStudents.get(i), allStudentScores.get(i), allSubmissionTimes.get(i));

                quizSubmissions.add(submission);

            }

        }

        return quizSubmissions;


    }


}
