# Ettore

Ettore is a platform for e-learning where there are two types of users: professors and students.The professors can create courses where they have the possibilities to upload courses that are written in Markdown. On the other part students can ask to join the courses that are taught by the professor and then be able to look at the lessons available in that course.

## Technology Stack

The project is written in **Java 11** with the support of **Gradle** v6.3 as a build tool and Spring Boot as a framework.The database that we decided to use is **H2** because it is an in-memory database and it is very easy to use.The project follows a MVC pattern and it is divided in three main packages: controller, model and templates(which are our view components).On the front-end we use **TailwindCSS** as a CSS framework and Thymeleaf as a template engine. For the testing phase we used Selenium for end2end test Junit4 with "bonigarcia webdrivermanager". **Lombok** was also used to reduce the amount of boilerplate code like getter/setter...

## Development process

We initially did a meeting to discuss which of the proposed project to develop and we decided to go with the e-learning platform. Then we started writing the stories and further developed their most interesting subsections into more detailed scenarios as later explained below.Then before starting with the actual development we created a **Figma** board on which we sketched out a basic design of the project. This helped us to have a clear idea of how the project should look like. It also allowed us to better estimate to time required to develop each page in the project.This way it was easier to divide the work among the team members and to assign each member a Task.

For managing the tasks we created a **Trello** board where we divided the tasks and we assigned them to the members of the team. We also decided to use the **GitFlow** workflow for coordinating the development on the repository hosted on **GitHub**.We created a branch for each feature and we merged them into the develop branch when they were ready.During the development phase we used **Discord** to communicate with each other and to coordinate the work every time that we felt the need to do so.Each branch contains both the code and the tests that we wrote for that feature.For a branch to be actually merged into develop a Pull Request had to opened by the author of the branch and the other members of the team had to review the code and possibly modify or refactor before finally approving the request.

### CI/CD

To ensure the quality of our codebase at all times we made use of the free CI/CD platform provided by **Github** called **Github Actions**. This allowed us to run the tests automatically every time a new commit was pushed to the repository or a Pull Request was created.This way we could be sure that the code we were pushing was working and that it was not breaking the existing codebase, giving us the peace of mind that the code provided by each member of the team was working as expected.

## Requirements

The Ettore platform must allow the following actions:

- User must be able to register and login to the platform with the role of professor or student
- Professor:

  - Must be able to create and edit a course
  - Must be able to create and edit lessons written in Markdown format
  - Must be able to approve or reject course subscription requests from students

- Student:

  - Must be able to search for courses
  - Must be able to request to subscribe to a course
  - Must be able to view the lessons of a course to which he is subscribed

## User Stories

### Story 1 - User registration, login and course creation (Gives rise to scenarios 1.1, 1.2 and 1.3)

Stefano is an ordinary professor and researcher at the Faculty of Computer Science of the University of Povegliano. Stefano can't wait for the semester to begin because he will teach for the first time a course on Signal analysis. Unfortunately, the University of Povegliano has not yet equipped itself with a system for distance learning, but Stefano would like to share with his students some in-depth writings that he made by himself on the geometric interpretation of the Fourier and Zeta transformations. Sending these contents via mail would be a feasible option, if not for the fact that Stefano would like to be able to modify the shared texts after publishing them, and also to welcome new students without having to continuously send mail. After some research on the web, Stefano discovers Ettore and decides to try it. **He registers by entering his credentials _(1.1)_**. **Then after logging in Ettore _(1.2)_ he starts creating his course on the platform _(1.3)_.**

### Story 2 - Course search, course subscription request and approval by the professor (Gives rise to scenarios 2.1, 2.2 and 2.3)

Filippo is a student at the Academy of Fine Arts of Pacengo, after having attended a talk organized by Dr. Stefano on low-level programming languages, and being fascinated by it, he decides to try to follow one of the courses tought by the same professor. Filippo unfortunately does not have the opportunity to follow Stefano's lessons because he is from another department but, after registering on Ettore, **he manages to find, through the dedicated search page, one of the courses that interests him a lot _(2.1)_**: "High-performance neural networks in assembly". After entering the platform, Filippo **subscribes to the course _(2.2)_** of the professor. Who, remembering the numerous interventions during the talk by Filippo, **accepts the course subscription _(2.3)_** to leave Filippo the opportunity to explore this new discipline.

### Story 3 - Upload of a lesson , lesson modification and lesson displaying(Gives rise to scenario 3.1, 3.2 and 3.3)

Professor Stefano receives several emails from working students who were unable to follow the lesson in which the Fast Fourier Transformation was explained. In addition, many other students have shown great interest on the subject. Stefano therefore decides to take advantage of Ettore and **publish on the page of his course "Signal analysis" several new lessons _(3.1)_** about the topic both introductory and in-depth. The lessons are in Markdown text format and this allows Stefano to enrich the content with images, videos, tables, mathematical formulas, etc. while remaining a file format of easy writing. **After publishing a lesson, the students have the possibility to open it _(3.2)_** . However later that day, Stefano realizes that one of the lessons had a typo in one of the formulas and decides to **modify the lesson again _(3.3)_**.

## Scenarios

### Scenario 1.1 - User registration

| Init Assumption | A person who is not yet registered on the online Ettore platform, decides to create their own account.  
| Normal Functioning | The subject goes to the Ettore homepage, this is the only page accessible to users who have not yet logged in. From this page the subject clicks on the "Register" button. At this point a form appears in which it is asked to enter the following information: name, surname, email address, password, confirm password and, finally, whether to register as a professor or as a student. These two modes decide the actions that the user will be able to perform on the platform once registered. To send the form, the "Register" button is clicked, which is only active if the form is correctly filled in. The user is redirected to the page with the list of courses (courses taught if the user is a professor, otherwise followed courses) which will initially be empty. |
| What can go wrong | The email entered is not valid. For our purposes, consider a valid email that has visible characters both to the left and to the right of an @ character. The check is done in real time and the "Register" button is not clickable if the email does not satisfy the format just described. The email used has already been registered. In this case, the "Register" button can be pressed but then a message of error is displayed. The form remains editable so as to be able to try again. The reason why this happens is that such a check can not be done client-side but it is necessary that the server interrogate the database. |

There is one or more empty fields in the form. In this case the "Register" button cannot be pressed.

The passwords entered in the two fields do not match or they are too short(<8). In this case the "Register" button cannot be pressed. |
| Other Activities | At the same time, another person could be registering a user with the same email address. To one of the two actors (the one who arrives last) an error message will be shown as explained above. |
| Final System State | There is a new registered user in the database. |

### Scenario 1.2 - User login

- Initial assumption | A person who is already registered on the Ettore platform, decides to log in.
- Normal functioning | The subject goes to the Ettore homepage, this is the only page accessible to users who have not yet logged in. From this page the subject clicks on the "Login" button. At this point a form appears in which it is asked to enter the following information: email address and password. To send the form, the "Login" button is clicked, which is only active if the form is correctly filled in. The user is redirected to the page with the list of courses (courses taught if the user is a professor, otherwise followed courses) which will initially be empty. |
- What can go wrong | The email entered is not valid. For our purposes, consider a valid email that has visible characters both to the left and to the right of an @ character. The check is done in real time and the "Login" button is not clickable if the email does not satisfy the format just described. The email used has not been registered. In this case, the "Login" button can be pressed but then a message of error is displayed.
- Other activities | A user can log in at the same time with two different devices. |
- Final system state | The user is logged in and can access the platform. |

### Scenario 1.3 - Creating a course

- Initial assumption | A user already registered and logged in on Ettore, with the role of "professor", would like to create a new course.
- Normal functioning | The user goes to the page where he can see the list of all his courses. At the beginning of the page there is a "New course" button that the user presses. A form opens in which the user must enter the fields: name, erogation year and category. Optionally, a description can also be added. The category is chosen from the options: mathematics, sciences, history, geography, art, music, and languages. When the form is complete, the user presses the "Create course" button. This button cannot be pressed unless all the mandatory fields have been entered. |
- What can go wrong | There is already a course with the same name and taught in the same year. This validation can not be done immediately; then the button can be pressed by the user. But the procedure does not go well and an error message is displayed on the screen. The form remains filled and available to be corrected before trying again. |
- Other activities | - |
- Final system state | There is a new course and it is linked to the user who is the protagonist of this scenario. |

### Scenario 2.1 - Searching a course

-Initial Assumption | A user is already registered and logged in on Ettore, with the role of student. He is on the page related to the search for courses and would like to search for a new course to which he can enroll. By default there are already some courses.|

- Normal functioning | The user uses the search filter to search for the desired courses, in the filter there are:
  - An input to search for keywords within the title or description of the course of interest
  - A dropdown that allows you to select at most one category of interest
  - A date input to search for the period of delivery of the course|
- What can go wrong | The user does not define any search criteria, in this case no action will be performed on the search page. If the course is not found,to the user will be shown an empty list wiht a message that no courses were found. |
- Other activities | While a student is looking for a course at the same time a professor is creating a new teaching. After the insertion of a new course by a professor all students have the opportunity to view such a course. |
- Final system state | - |

### Scenario 2.2 - Requesting to attend a course

- Initial assumption | A user is already registered and logged in on Ettore, with the role of student and has already performed the search for a course.
- Normal functioning | The student, having found the course of interest, clicks the "Join" button. Once the enrollment request has been made, the student will be redirected into his list of joined courses. In future searces the "Join" button, after making the request, will be disabled. In case of rejection by the professor, the student will be able to Join again via the same procedure. |
- What can go wrong | The "Join" button is disabled, this means that the request for enrollment in the course has already been made, the student can do nothing but wait for feedback from the professor. |
- Other activities | - |
- Final system state | The course is linked to a new enrollment request from the student who performed the action. |

### Scenario 2.3 - Accepting/Reject a student's request to join a course or removing a student from a course

- Initial assumption | A user is already registered and logged in on Ettore, with the role of professor and is on the page with the list of students for a specific course and there is a request to join the course from a student. |
- Normal functioning | The professor, in the list of student requests for a specific course menu, has the possibility to manage all the participants of the course (accept / reject enrollments, remove students already enrolled). In the "Join requests" tab there is a list of students (in tabular mode) waiting for confirmation of enrollment. On the right of the line of a student there are two buttons: "Accept" and "Reject". The professor, based on the selected action, allows the requesting student to participate or not in the course. In case of confirmation, the student will have the opportunity to see the professor's course in the "My courses" section, otherwise the student can request enrollment in the course again. |
- What can go wrong | If there are no students making a request to join, to the professor will be shown an empty tab of requests or if there are no students enrolled the student tab will be empty. If there are many users making a request to join, the professor will have to manually confirm all the requests waiting for approval. A lot of manual work to do. |
- Other activities | A student makes a request to join a course and the professor is on the "Manage enrollments" page, in this case the professor does not have the possibility to see the new enrollment unless he refreshes the page. |
- Final system state | On the student side there is a new course to which it is possible to access. On the course side, there is a new enrolled student. |

### Scenario 3.1 - Creating a new Lesson

- Initial assumption | A user is already registered and logged in on Ettore, with the role of professor, wants to upload a new lesson in one of the courses managed by him/her. |
- Normal functioning | The user goes to the page related to the course and navigates to the "Lessons" section and clicks on "New lesson" button. The system asks the professor a title (mandatory), a description (optional) and the content(mandatory) which can be written in `Markdown` format. When at least the title and the content are present, the user can click on a "Save" button that creates the new lesson. |
- What can go wrong | The professor does not enter a title or the content of the lesson, in this case the system will not allow the professor to save the lesson , or the professor enters a title that is already present in the course, in this case the system will add "(1)" at the end of the title. |
- Other activities | A student watching the list of lessons while the professor is adding a new lesson. In this case the student cannot see the new lesson without refreshing the page.|
- Final system state | The lesson is linked to the course and can be viewed by the students enrolled in the course. |

### Scenario 3.2 - Viewing a lesson

- Initial assumption | A user is already registered and logged in on Ettore (professor or student), wants to view a lesson of a course to which he/she has access. |
- Normal functioning | The user goes to the page related to the course and navigates to the "Lessons" section and clicks on the lesson he/she wants to view. The system shows the lesson's title, description and content which is rendered in markdown to show elements such as images, tables, lists, etc. |
- What can go wrong |---|
- Other activities | A student watching the list of lessons while the professor is editing or removing a lesson. In this case the student cannot see the correct list of lessons without refreshing the page.|
- Final system state |---|

### Scenario 3.3 - Editing a lesson

- Initial assumption | A user is already registered and logged in on Ettore, with the role of professor, wants to edit a lesson of his courses on the course list. |
- Normal functioning | The user goes to the page related to the course and navigates to the "Lessons" list section and clicks on the title of lesson he/she wants to edit. The system shows the lesson's title, description and content. The professor can click on "edit" button to go to the modifying page of the lesson and then be able to change the title, description and content of the lesson. When the professor is done editing the lesson he/she can click on a "Save" button that saves the changes. |
- What can go wrong | The professor does not enter a title or the content of the lesson, in this case the system will not allow the professor to save the lesson , or the professor enters a title that is already present in the course, in this case the system will add "(1)" at the end of the title. |
- Other activities | A student watching the list of lessons while the professor is editing a lesson. In this case the student cannot see the correct list of lessons without refreshing the page.|
- Final system state | The edited lesson is now updated with the new information entered by the professor. |

## Authentication system

The spring boot authentication system was very complex to implement so we decide to create our own authentication system.
Upon logging in, the user's client is sent a pair of cookies containing his/her email and the other containing the hash of the password.These cookies are then sent along each following request.there is then an Interceptor that checks if the cookies are present and if they are valid. If they are not valid the user is redirected to the login page.The handler of the logout request simply deletes the cookies.

## Code Quality Assurance

To guarantee a codebase of high quality we used automated tests. We used **JUnit** for unit testing and **Selenium** for end2end testing.This allowed us to have a clear idea of the quality of our code and to improve it if necessary by periodically making improvements and refactoring the code to make it more readable and maintainable.

In total we have **130** test dived in two main categories:

- **Unit tests**: 58
  These are mainly used to test the logic of our models since there we have code that checks that some guarantees are always respected. For example, we have a test that checks that the a student can not be accpeted to join a course if he/she has never requested to join it.

- **End2end tests**: 72
  These are used to test the logic of our controllers and the interaction between the different components of the project and to check different parts of the code that were unreachable with unit tests.

## Unit tests

Here we have a list of the most immportant unit tests that we have implemented:

-
-

## End2end tests

Here we have a list of the most immportant end2end tests that we have implemented:

-
-

## Code Coverage
