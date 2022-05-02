# CS-180-Group-Project-5
CS 180 Final project

### Instructions on how to run and compile project
Take the all of the classes within our compository and all of the .txt files to run and compile the program. If you decide to make a new .txt file to upload, make sure it is in the specific format that our program takes. The format for the gradebook is student name, quiz name, score on the exam, YYYY/MM/DD;HH:mm:ss.

Things changed from project 4 for point back--- timestaps have been added, mutiple questions on quizzes, grading is fixed, student and teacher accounts operating the same have been fixed, students can upload a file to submit a quiz, students can take the quizes mutiple times, randomization of questions and quizzes have been fixed. 

### Anvit Sinha - Submitted Vocareum Workspace, Anvit Sinha - Submitted Report on brightspace, Anvit Sinha - Submitted presnetation on brightspace

MainMenu.java:
The main method class handles all the GUI input/output for the user. This sends the request to the MultiClientServer.java which routes the request to MainServer.java.
The class then gets the response from the server and based on this response shows the appropriate GUI to the user.,
The main menu allows the users to create a new account or log into an existing one. 
When the user’s sign up, they are allowed to choose whether their user type (student/teacher).
The class controls the flow of the program based on the type of the student.
The main student/teacher menu remains the same for both users, with an option to change account settings or enter the quizzes menu. The account settings functionality works the same regardless of the user type.
A different quiz menu is displayed based on the user’s type 
For the teacher’s quiz menu,  they have an option to select or add a course.
Within each course, they can freely manipulate the quizzes associated with that quiz.
For the student’s quiz menu, they are given options to view their grade, their overall rank, total quizzes attempted, view overall average, and to select a course.
Once a course is selected, they can view all the assigned quizzes, view all the attempts taken from all students, and also attempt a quiz themselves.
The quiz is assigned RANDOMLY from the entire course’s quizbank.
Based on the student’s answer, their grades are updated.
At all points, appropriate error messages are shown if any error occurs.


Account.java:
Reads and stores the database of the different login information
Determines whether the person that was trying to log into our learning management system is a teacher or student based on the stored data from the database. 
If it was determined that the user was a student, the terminal would output the student’s menu. If the user was a teacher, it would output the teacher’s terminal (handled in MainMenu.java).
If the user is neither of those options, the class would return a false result
Based on if the user wants to change login credentials, this class will take in the user’s new password, username or name and set it to the old version of those. This way the database will be updated with the newest version of the login credentials the user wanted.  
The account class also provides a method to update a user’s username, full name, or password. It even has a check to see if a new username already exists in the database and shows appropriate error messages.


Gradebook.java
The class provides methods to calculate average scores, update scores etc.
This class also handles adding a new student to the gradebook if a new account for a student is created.
This class also keeps track of all the attempts for each course, and also how many quizzes are within each course.
This is where the timestamps for each submission are added.
This class provides methods to calculate all the different types of scores that the client can request.
The server calls the appropriate method for each request


MainServer.java
Has the control flow for each request sent from the user.
Has temporary storage for what the user is working with at the moment.
Uses a switch statement where each case is every request that the user can make.
Each request is routed to the appropriate class where the method is called to perform what is needed.
All results are then sent to the MainMenu class to show to the user.

MultiClientServer.java 
Handles creating a new thread for each new user. 
Stays running until it is manually shut down
Uses principles of concurrency to have multiple users access the server at a time.


Question.java 
Creates a question object which can be added to a quiz. 
Each question has a name, answer choices, and the correct answer associated with them, which can all be accessed by the servers and other methods as needed.
Also has helpful methods that can be used by other classes to manipulate or work with individual questions.

Quiz.java
Creates a quiz object that the user can interact with.
Each quiz has its associated questions, course, and name all of which can be accessed as needed.

QuizFile.java
Reads from the quizDatabase file to initialize all the quizzes.
Has the methods to coherently make the quiz and question classes work together.
Allows the server to call any of its methods to manipulate, add, or attempt quizzes or questions.

Course.java
This class handles all of the Course methods in one class. 
It handles adding, removing, and updating courses as they are edited by the user.
All data for courses is stored in files, and then read onto static arraylists so as to not store any data in the client side.
It also has methods that the server can call to help the client with specific requests about each course.
Each course has an associated course number and number of quizzes.


