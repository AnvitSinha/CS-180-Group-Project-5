import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class GradeBook {

    private static final String studentGradebook = "GradeBook.txt";
    public static ArrayList<String> allGradebookStudents = new ArrayList<>();   // Names of students in the gradebook
    public static ArrayList<String> allQuizNames = new ArrayList<>();           // Name for each quiz taken in order
    public static ArrayList<Double> allStudentScores = new ArrayList<>();       // Score for each quiz in order
    public static ArrayList<String> allCourseName = new ArrayList<>();          // Name of all associated courses
    public static ArrayList<String> allSubmissionTimes = new ArrayList<>();  // All Submission times in order

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


    }

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

    public static int calculateStanding(double score) {

        int position = 1;

        for (Double i : allStudentScores) {

            if (score < i) {

                position += 1;

            }

        }

        return position;

    }   // TODO

    public static double calculateCourseAverage(String courseName) {

        double sum = 0;
        int num = 0;

        for (int i = 0; i < allCourseName.size(); i++) {

            if (allCourseName.get(i).equals(courseName)) {      // If the course at the index is the one to check

                sum += allStudentScores.get(i);     // Add the score in that position to the total sum
                num++;

            }

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

        return (sum / num);

    }   //Find student's average in course

    public static boolean addNewSubmission(String studentName, String quizName, double score, String courseName) {

        allGradebookStudents.add(studentName);
        allQuizNames.add(quizName);
        allStudentScores.add(score);
        allCourseName.add(courseName);

        try (PrintWriter pw = new PrintWriter(new FileWriter(studentGradebook, true))) {

            pw.println(String.format("%s,%s,%.2f,%s", studentName, quizName, score, courseName));

            return true;

        } catch (IOException e) {

            return false;

        }

    }

    public static ArrayList<String> viewQuizSubmissions(String quizName) {

        ArrayList<String> quizSubmissions = new ArrayList<>();

        for(String names : allQuizNames) {

            if (names.equalsIgnoreCase(quizName)) {

                int num = allQuizNames.indexOf(names);

                String submission = String.format("Student: %s\nScore: %.2f\nSubmitted at: %s\n",
                        allGradebookStudents.get(num), allStudentScores.get(num), allSubmissionTimes.get(num));

                quizSubmissions.add(submission);


            }

        }

        return quizSubmissions;


    }


}
