package it.ettore.e2e;

import it.ettore.e2e.po.ErrorsComponent;
import it.ettore.e2e.po.LoginPage;
import it.ettore.e2e.po.RegisterPage;
import it.ettore.model.User;
import it.ettore.model.UserRepository;
import it.ettore.e2e.po.professor.courses.ProfessorCoursesPage;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.*;

public class Register extends E2EBaseTest {
    @Autowired
    private UserRepository repoUser;

    @Test
    public void allFieldsMustBeFilled() {
        driver.get(baseDomain() + "register");
        assertEquals("I'm supposed to be in /register", "/register", currentPath());
        RegisterPage registerPage = new RegisterPage(driver);

        Runnable assertNotClickable = () -> {
            assertFalse("Register button shouldn't be clickable yet", registerPage.isRegisterClickable());
        };

        // Assert button is not clickable initially
        assertNotClickable.run();

        // One by one, insert the data and check that the button becomes clickable only at the end
        registerPage.setFirstName("Human");
        assertNotClickable.run();

        registerPage.setLastName("Being");
        assertNotClickable.run();

        registerPage.setEmail("human.being@earth.space");
        assertNotClickable.run();

        registerPage.setPassword("haha_gotcha");
        assertNotClickable.run();

        registerPage.setConfirmPassword("haha_gotcha");

        // Now clickable
        assertTrue("Register button should be clickable when all fields are filled out", registerPage.isRegisterClickable());

        registerPage.tickProfessor();
        registerPage.registerAsProfessor();
        assertEquals("I'm supposed to be in /professor/courses", "/professor/courses", currentPath());
    }

    @Test
    public void emailMustBeValid() {
        driver.get(baseDomain() + "register");
        assertEquals("I'm supposed to be in /register", "/register", currentPath());
        RegisterPage registerPage = new RegisterPage(driver);

        // Start registering a new user with an invalid email
        registerPage.setFirstName("Definitely");
        registerPage.setLastName("Real Human");
        registerPage.setEmail("earth_invasion_attempt");
        registerPage.setPassword("alien_spy");
        registerPage.setConfirmPassword("alien_spy");

        // Assert error is shown
        assertTrue(registerPage.isInvalidEmailFormatVisible());
        // Assert button is not clickable
        assertFalse(registerPage.isRegisterClickable());

        // Now insert a valid email
        registerPage.setEmail("these.humans@are.so.smart.com");

        // Assert error is not shown anymore
        assertFalse(registerPage.isInvalidEmailFormatVisible());

        // Assert button is now clickable
        assertTrue(registerPage.isRegisterClickable());

        // Click the register button and conclude the registration
        registerPage.tickProfessor();
        registerPage.registerAsProfessor();

        // Check that the registration redirected us correctly
        assertEquals("I'm supposed to be in /professor/courses", "/professor/courses", currentPath());
    }

    @Test
    public void passwordMustBeLongEnough() {
        driver.get(baseDomain() + "register");
        assertEquals("I'm supposed to be in /register", "/register", currentPath());
        RegisterPage registerPage = new RegisterPage(driver);

        // Start registering a new user with a short password
        registerPage.setFirstName("Definitely");
        registerPage.setLastName("Real Human");
        registerPage.setEmail("earth@space.com");
        registerPage.setPassword("spy");
        registerPage.setConfirmPassword("spy");

        // Assert error is shown
        assertTrue(registerPage.isPasswordTooShortVisible());
        // Assert button is not clickable
        assertFalse(registerPage.isRegisterClickable());

        // Now let's try with a long enough password
        registerPage.setPassword("alien_spy");
        registerPage.setConfirmPassword("alien_spy");

        // Assert error is not shown anymore
        assertFalse(registerPage.isPasswordTooShortVisible());
        // Assert button is now clickable
        assertTrue(registerPage.isRegisterClickable());

        // Click the register button and conclude the registration
        registerPage.tickProfessor();
        registerPage.registerAsProfessor();
        assertEquals("I'm supposed to be in /professor/courses", "/professor/courses", currentPath());
    }

    @Test
    public void passwordsMustMatch() {
        driver.get(baseDomain() + "register");
        assertEquals("I'm supposed to be in /register", "/register", currentPath());
        RegisterPage registerPage = new RegisterPage(driver);

        // Start registering a new user with a short password
        registerPage.setFirstName("Definitely");
        registerPage.setLastName("Real Human");
        registerPage.setEmail("earth@space.com");
        registerPage.setPassword("alien_spy");
        registerPage.setConfirmPassword("alien_spy_ohno");

        // Assert error is shown
        assertTrue(registerPage.isPasswordsDontMatchVisible());
        // Assert button is not clickable
        assertFalse(registerPage.isRegisterClickable());

        // Now use the same password
        registerPage.setConfirmPassword("alien_spy");

        // Assert error is not shown anymore
        assertFalse(registerPage.isPasswordsDontMatchVisible());
        // Assert button is now clickable
        assertTrue(registerPage.isRegisterClickable());

        // Click the register button and conclude the registration
        registerPage.tickProfessor();
        registerPage.registerAsProfessor();
        assertEquals("I'm supposed to be in /professor/courses", "/professor/courses", currentPath());
    }

    @Test
    public void cannotRegisterSameEmailTwice() {
        driver.get(baseDomain() + "register");
        assertEquals("I'm supposed to be in /register", "/register", currentPath());
        RegisterPage registerPage = new RegisterPage(driver);

        // Register once
        registerPage.setFirstName("Real");
        registerPage.setLastName("Human");
        registerPage.setEmail("real.human@earth.com");
        registerPage.setPassword("alien_spy");
        registerPage.setConfirmPassword("alien_spy");

        registerPage.tickProfessor();
        ProfessorCoursesPage coursesPage = registerPage.registerAsProfessor();
        assertEquals("I'm supposed to be in /professor/courses", "/professor/courses", currentPath());

        LoginPage loginPage = coursesPage.headerComponent().logout();
        registerPage = loginPage.register();
        assertEquals("I'm supposed to be in /register", "/register", currentPath());

        // Try to register again, use the same email
        registerPage.setFirstName("Definitely Human");
        registerPage.setLastName("Being");
        registerPage.setEmail("real.human@earth.com");
        registerPage.setPassword("alien_spy");
        registerPage.setConfirmPassword("alien_spy");

        registerPage.tickProfessor();
        registerPage.registerAsProfessor();

        // Should be still in /register
        assertEquals("I'm supposed to be in /register", "/register", currentPath());
        // Assert error is shown
        ErrorsComponent errors = new ErrorsComponent(driver);
        assertEquals(Set.of("Email already taken"), errors.getErrorMessageSet());
        // Dismiss the error
        errors.getErrors().get(0).dismiss();
        // Should now have no errors displayed
        assertEquals(Set.of(), errors.getErrorMessageSet());
    }

    /**
     * Test that two users can have the same password
     */
    @Test
    public void twoUsersCanHaveSamePassword() {
        String password = "same_password";

        repoUser.save(new User("Some", "Human", "some.human@earth.com", password, User.Role.PROFESSOR));

        driver.get(baseDomain() + "register");
        RegisterPage registerPage = new RegisterPage(driver);

        String anotherUserEmail = "another.human@earth.com";

        registerPage.setFirstName("Another");
        registerPage.setLastName("Human");
        registerPage.setEmail(anotherUserEmail);
        registerPage.setPassword(password);
        registerPage.setConfirmPassword(password);
        registerPage.tickProfessor();
        ProfessorCoursesPage professorCoursesPage = registerPage.registerAsProfessor();

        LoginPage loginPage = professorCoursesPage.headerComponent().logout();

        assertEquals("I'm supposed to be in /login", "/login", currentPath());

        // Should be able to login to the second user that has the same email
        loginPage.setEmail(anotherUserEmail);
        loginPage.setPassword(password);
        professorCoursesPage = loginPage.loginAsProfessor();
        assertEquals("Another Human", professorCoursesPage.headerComponent().getFullName());
    }

    /**
     * Tests that a student can register him/herself
     */
    @Test
    public void registerStudent() {
        driver.get(baseDomain() + "register");
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.setFirstName("Marco");
        registerPage.setLastName("Rossi");
        registerPage.setEmail("marco.rossi@univr.it");
        registerPage.setPassword("Some password");
        registerPage.setConfirmPassword("Some password");
        registerPage.tickStudent();
        registerPage.registerAsStudent();

        // Should now be in the student's homepage
        assertEquals("/student/courses", currentPath());
    }

    /**
     * Tests that a professor can register him/herself
     */
    @Test
    public void registerProfessor() {
        driver.get(baseDomain() + "register");
        RegisterPage registerPage = new RegisterPage(driver);

        registerPage.setFirstName("Marco");
        registerPage.setLastName("Rossi");
        registerPage.setEmail("marco.rossi@univr.it");
        registerPage.setPassword("Some password");
        registerPage.setConfirmPassword("Some password");
        registerPage.tickProfessor();
        registerPage.registerAsStudent();

        // Should now be in the professor's homepage
        assertEquals("/professor/courses", currentPath());
    }
}
