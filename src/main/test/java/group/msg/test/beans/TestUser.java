package group.msg.test.beans;

import group.msg.entities.User;
import group.msg.jsf_beans.BugBean;
import group.msg.jsf_beans.UserEditBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.meanbean.test.BeanTester;

public class TestUser {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void shouldHaveNoViolations() {
        //given:
        User user = new User();

        user.setFirstName("ana");
        user.setLastName("mara");
        user.setEmail("ana.mara@msggroup.com");
        user.setMobileNumber("+40456789011");

        //when:
        Set<ConstraintViolation<User>> violations
                = validator.validate(user);

        //then:
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldDetectInvalidEmail() {

        //given:
        User user = new User();

        user.setEmail("a");
        user.setMobileNumber("+40456789011");

        //when:
        Set<ConstraintViolation<User>> violations
                = validator.validate(user);

        //then:
        assertEquals(violations.size(), 1);

        ConstraintViolation<User> violation
                = violations.iterator().next();
        assertEquals("Email validation did not pass!",
                violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("a", violation.getInvalidValue());
    }

    @Test
    public void shouldDetectInvalidMobileNumber() {
        //give:
        User user = new User();
        user.setEmail("Don.Joe@msggroup.com");
        user.setMobileNumber("a");

        //when:
        Set<ConstraintViolation<User>> violations
                = validator.validate(user);

        //then:
        assertEquals(violations.size(), 1);

        ConstraintViolation<User> violation
                = violations.iterator().next();

        assertEquals("Mobile number validation did not pass!",
                violation.getMessage());
        assertEquals("mobileNumber", violation.getPropertyPath().toString());
        assertEquals("a", violation.getInvalidValue());
    }
}