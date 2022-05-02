public class Trial {

    public static void main(String[] args) {

        try {

            QuizFile q = new QuizFile("quizDatabase.txt");

            System.out.println(q.numCorrectAns("C:\\Users\\anvit\\Desktop\\QuizSubmission.txt"));


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
