package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.entities.*;
import lombok.Data;
import org.jboss.weld.context.ejb.Ejb;
import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.LinkedList;

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
    private String roleString;
    private String deleteRoleString;
    private User user = (User) WebHelper.getSession().getAttribute("currentUser");

    @EJB
    private UserServiceEJB userServiceEJB;

    @EJB
    private NotificationServiceEJB notificationServiceEJB;

    @EJB
    private RoleServiceEJB roleServiceEJB;

    @Inject
    private PasswordEncryptor passwordEncryptor;

    public String update() {

        String info = "";

        if (firstName.length() > 0) {
            info += "First name: old=" + user.getFirstName() + " new=" + firstName + " ";
            user.setFirstName(firstName);

        }
        if (lastName.length() > 0) {
            info += "Last name: old=" + user.getLastName() + " new=" + lastName + " ";
            user.setLastName(lastName);
        }
        if (email.length() > 0) {
            info += "Email: old=" + user.getEmail() + " new=" + email + " ";

            user.setEmail(email);
        }
        if (mobileNumber.length() > 0) {
            info += "Mobile Number: old=" + user.getMobileNumber() + " new=" + mobileNumber + " ";
            user.setMobileNumber(mobileNumber);
        }

        if (password.length() > 0) {
            String encryptedOldPassword = passwordEncryptor.passwordEncryption(oldPassword);
            if (encryptedOldPassword.equals(user.getPassword())) {
                info += "Password: old=" + oldPassword + " new=" + password + " ";
                user.setPassword(passwordEncryptor.passwordEncryption(password));
            } else {

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Wrong old password");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
                return "account";
            }

        }
        if (roleString!=null) {


            if (userServiceEJB.userHasRight(user, RightType.USER_MANAGEMENT)) {
                userServiceEJB.addRole(RoleType.valueOf(roleString), user);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Rights", "Required right: " + "USER_MANAGEMENT");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }

        }
        if (deleteRoleString!=null) {

            if (userServiceEJB.userHasRight(user, RightType.USER_MANAGEMENT)) {
                    info="111111111111111";
                    Role role=new Role(RoleType.valueOf(deleteRoleString));
                    userServiceEJB.deleteRoleFromUser(role,user.getUsername());
            }
            else{
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Rights", "Required right: " + "USER_MANAGEMENT");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }


        }

        userServiceEJB.update(user);
        Notification notification = new Notification(NotificationType.USER_UPDATED);
        notification.setInfo(info);
        notificationServiceEJB.save(notification);
        user.getNotifications().add(notification);

        return "account";
    }


}
