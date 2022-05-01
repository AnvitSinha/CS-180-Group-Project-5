Test 1: Test Invalid Log in 

Steps:
1. User starts the MultiClientServer and MainMenu
2. User selects the login Button
3. User selects the username textbox
4. User enters "ARizzo" via keyboard
5. User selects the password textbox
6. User enters "44" via keyboard
7. User selects the OK button

Expected Result- Application shows an Inavlid Credentials, Try again error message. Also prompts the user to select the OK button to return to log in page.

Test Status: Passed


Test 2: Test Account Creation

Steps: 
1. User starts the MultiClientServer and MainMenu
2. User selects the Sign Up button
3. User selects the Teacher button
4. User selects the Name textbox 
5. User enter "Anthony Rizzo" via keyboard
6. User selects the Usernname textbox 
7. User enter "ARizzo" via keyboard
8. User selects the password textbox 
9. User enters "44" via keyboard
10. User selects the ok Button
11. User selects the ok Button again 
12. User selects the login Button
13. User selects the username textbox
14. User enter "ARizzo" via keyboard
15. User selects the password textbox
16. User enters "44" via keyboard
17. User selects the OK button

Epexted Result- Application shows a succesfull creation message for the account after creating the account, then after using those credentials, the application should the main menu option for that account. 

Test Status- Passed

Test 3: Test Teacher login with changing account details

Steps:
1. User starts the MultiClientServer and MainMenu
2. User selects the login Button
3. User selects the username textbox
4. User enters "kdarabhy" via keyboard
5. User selects the password textbox
6. User enters "proj4kdarabhy" via keyboard
7. User selects the OK button
8. User selects the Ok Button
9. User selects the Account Settings button
10. User selects the Edit account button
11. User selects change password button
12. User selects the enter new password textbox 
13. User delets the "Enter new password text" via keyboard
14. User enters in "proj5kedar" via keyboard
15. User selects the OK button
16. User selects the Exit Button
17. User selects the Exit Button

Expected result- The Application should show the following menus in order- the main menu with the login button, the succesful login message, the teacher main menu, the teacher account menu, the teacher update account menu, the enter password page, and then the normal teacher account log in page.

Test Status - Passed


Test 4: Test teacher login with delete account 

Steps:
1. User starts the MultiClientServer and MainMenu
2. User selects the login Button
3. User selects the username textbox
4. User enters "kdarabhy" via keyboard
5. User selects the password textbox
6. User enters "proj4kdarabhy" via keyboard
7. User selects the OK button
8. User selects the Ok Button
9. User selects the Account Settings button
10. User selects the delete account button
11. User selects the OK button
12. User selects the OK button
13. User selects the exit button

Expected Result- The application should show the follwing menus in order- the main menu with the login button, the succesful login message, the teacher main menu, the teacher account menu, the delete account button, the are you sure delete account message, then the main menu. 

Test Status - Passed


Test 5: Test teacher upload file for quiz

Steps:
1. User starts the MultiClientServer and MainMenu
2. User selects the login Button
3. User selects the username textbox
4. User enters "kdarabhy" via keyboard
5. User selects the password textbox
6. User enters "proj4kdarabhy" via keyboard
7. User selects the OK button
8. User selects the courses button 
9. User selects the select course option from the dropdown 
10. User selects the OK button
11. User selects the CS180 option from the dropdown
12. User selects the OK button
13. User selects the add quiz button 
14. User selects the add quiz file button
15. User selects a .txt for a quiz file in our specific format

Expected result- the application should show the following menus in order- the main menu with the login button, the succesful login message, the teacher main menu, the select course menu, add quiz file. The program should also pop up a local file loader for the computer. After you select the file, you should see a file quiz added sucess message. 

Test Status - Passed

Test 6: Student logging in to upload a file asnwer to a quiz 

Steps:
1. User starts the MultiClientServer and MainMenu
2. User selects the login Button
3. User selects the username textbox
4. User enters "georgewu" via keyboard
5. User selects the password textbox
6. User enters "gwuproj4" via keyboard
7. User selects the OK button
8. User selects the courses button
9. User selects the select course option from the dropdown menu
10. User selects the OK button 
11. User selects the CS180 course from the dropdown menu
12. User selects the OK button
13. User selects the list quizes button
14. User selects the data types quiz from the dropdown menu
15. User selects the OK button
16. User selects the Attempt quiz button
17. User selects the OK button
18. User selects a file to upload for the answeers for the quiz

Expected Result - the application should show the following menus in order- the main menu with the login button, the succesful login message, the student main menu, the select course menu, the student course menu, the quiz dropdown menu, the quiz, and finally the file upload for the answers. 

Test Status - Passed 

Test 7: Student log in to see Grade 

Steps:
1. User starts the MultiClientServer and MainMenu
2. User selects the login Button
3. User selects the username textbox
4. User enters "georgewu" via keyboard
5. User selects the password textbox
6. User enters "gwuproj4" via keyboard
7. User selects the OK button
8. User selects the courses button
9. User selects the select course option from the dropdown menu
11. User hits the OK button
12. User selects the your course grade button

Expected result- The application should show the grade of the course based on the course and the quiz grade. Both of those can be found the gradebook.txt file. 

Test Status - Passed


