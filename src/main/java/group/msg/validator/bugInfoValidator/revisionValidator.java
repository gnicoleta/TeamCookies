package group.msg.validator.bugInfoValidator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class revisionValidator implements ConstraintValidator<revisionValidation, String> {

    @Override
    public boolean isValid(String description, ConstraintValidatorContext constraintValidatorContext) {

        if (null == description) {
            return false;
        }

        char[] charArray = description.toCharArray();

        int size = charArray.length;
        for (int i = 0; i < size; i++) {
            if (Character.isLetterOrDigit(charArray[i])) {
                if (i + 1 < size && charArray[i + 1] != '.') {
                    return false;
                }
            } else {
                if (charArray[i] == '.') {
                    if (i + 1 < size && !Character.isLetterOrDigit(charArray[i + 1])) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }
}
