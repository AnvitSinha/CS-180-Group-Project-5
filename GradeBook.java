import java.io.*;
import java.util.ArrayList;

public class GradeBook {

    private static String studentGradebook = "student_grades.txt";
    public static ArrayList<String> allGradebookStudents = new ArrayList<>();      // Names of students in the gradebook
    public static ArrayList<Integer> allStudentAttempts = new ArrayList<>();    // Attempts for each student
    public static ArrayList<Double> allStudentGrades = new ArrayList<>();      // Grades of each student

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

}
