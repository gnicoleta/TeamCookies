package group.msg.validator.mobileNumberValidation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = mobileNumberValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface mobileNumberValidation {

    String message() default "Mobile number validation did not pass!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
