package group.msg.userManagement.validator.mobileNumberValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class mobileNumberValidator implements ConstraintValidator<mobileNumberValidation, String> {


    private boolean isRomanianPhoneNumber(String mobileNumber){
        String startingPathRO="+40";
        int romanianPhoneNumberLength=10;

        if(mobileNumber.startsWith(startingPathRO)){
            if(mobileNumber.length()==romanianPhoneNumberLength){
                return true;
            }
        }

        return false;
    }

    private boolean isGermanPhoneNumber(String mobileNumber){
        String startingPathDe="+49";
        int germanPhoneNumberLength=12;

        if(mobileNumber.startsWith(startingPathDe)){
            if(mobileNumber.length()==germanPhoneNumberLength){
                return true;
            }
        }

        return false;
    }


    @Override
    public boolean isValid(String mobileNumber, ConstraintValidatorContext constraintValidatorContext) {

        if(isRomanianPhoneNumber(mobileNumber)){
            return true;
        }
        else{
            if(isGermanPhoneNumber(mobileNumber)){
                return true;
            }
        }

        return false;
    }

}
