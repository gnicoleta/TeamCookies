package group.msg.test.beans;

import group.msg.entities.Bug;
import group.msg.entities.User;
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

public class TestBug {

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
        Bug bug = new Bug();

        bug.setDescription("Minimum 10 chars");
        bug.setVersion("2.0");

        //when:
        Set<ConstraintViolation<Bug>> violations
                = validator.validate(bug);

        //then:
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldDetectInvalidDescription() {

        //given:
        Bug bug = new Bug();

        bug.setDescription("a");
        bug.setVersion("2.0");

        //when:
        Set<ConstraintViolation<Bug>> violations
                = validator.validate(bug);

        //then:
        assertEquals(violations.size(), 1);

        ConstraintViolation<Bug> violation
                = violations.iterator().next();
        assertEquals("Description validation did not pass!",
                violation.getMessage());
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("a", violation.getInvalidValue());
    }

    @Test
    public void shouldDetectInvalidVersion() {
        //given:
        Bug bug = new Bug();

        bug.setVersion("abc");
        bug.setDescription("Minimum 10 chars");

        //when:
        Set<ConstraintViolation<Bug>> violations
                = validator.validate(bug);

        //then:
        assertEquals(violations.size(), 1);

        ConstraintViolation<Bug> violation
                = violations.iterator().next();

        assertEquals("Revision validation did not pass!",
                violation.getMessage());
        assertEquals("version", violation.getPropertyPath().toString());
        assertEquals("abc", violation.getInvalidValue());
    }
}