package group.msg.userManagement;

import lombok.Data;

import java.util.List;
import group.msg.userManagement.validator.emailValidation.emailValidation;
import group.msg.userManagement.validator.mobileNumberValidation.mobileNumberValidation;

@Data
public class User {

    private String familyName;
    private String givenName;

    @mobileNumberValidation
    private String mobileNumber;

    @emailValidation
    private String email;

    private List<Role> roles;

    private String userName;

    public User() {
    }

    public User(String familyName, String givenName, String mobileNumber, String email) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    
}
