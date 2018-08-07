package group.msg.validator.bugInfoValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class descriptionValidator implements ConstraintValidator<descriptionValidation, String> {

    @Override
    public boolean isValid(String description, ConstraintValidatorContext constraintValidatorContext) {

        if(null==description){
            return false;
        }

        return description.length() > 9;

    }
}
