package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.entities.User;
import lombok.Data;
import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@ViewScoped
public class AccountJSFBean implements Serializable {
    private String firstName;
    private String lastName;

    private String email;
    private String mobileNumber;
    private String oldPassword;
    private String password;
    private  User user=(User) WebHelper.getSession().getAttribute("currentUser");

    @EJB
   private UserServiceEJB userServiceEJB;

    @Inject
    private PasswordEncryptor passwordEncryptor;

    public String update(){

        if(firstName.length()>0) {
            user.setFirstName(firstName);
        }
        if(lastName.length()>0){
            user.setLastName(lastName);
        }
        if(email.length()>0) {
            user.setEmail(email);
        }
        if(mobileNumber.length()>0){
            user.setMobileNumber(mobileNumber);
        }

        if(password.length()>0){
            String encryptedOldPassword=passwordEncryptor.passwordEncryption(oldPassword);
            if(encryptedOldPassword.equals(user.getPassword()))
            {
                user.setPassword(passwordEncryptor.passwordEncryption(password));
            }
           else{

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Wrong old password");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
                return "account";
            }

        }

        userServiceEJB.update(user);
        return "account";
    }

}
