package group.msg.validator.emailValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class emailValidator implements ConstraintValidator<emailValidation, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

        String path="@msggroup.com";

        if(email.endsWith(path)){
            return true;
        }

        return false;
    }

}
