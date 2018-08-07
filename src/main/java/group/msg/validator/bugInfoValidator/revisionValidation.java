package group.msg.validator.bugInfoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = revisionValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface revisionValidation {

    String message() default "Revision validation did not pass!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
