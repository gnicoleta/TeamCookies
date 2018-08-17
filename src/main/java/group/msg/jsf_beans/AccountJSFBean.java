package group.msg.jsf_beans;


import group.msg.beans.PasswordEncryptor;
import group.msg.entities.*;
import lombok.Data;

import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.Collection;


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
    private RightServiceEJB rightServiceEJB;

    @EJB
    private NotificationServiceEJB notificationServiceEJB;

    @EJB
    private RoleServiceEJB roleServiceEJB;

    @Inject
    private PasswordEncryptor passwordEncryptor;

    public String update() {

        String info = "";

        if (firstName.length() > 0) {
            info += "First Name changed to: (new)" + firstName + " from (old)" + user.getFirstName();
            user.setFirstName(firstName);

        }
        if (lastName.length() > 0) {
            info += "Last Name changed to: (new)" + lastName + " from (old)" + user.getLastName();
            user.setLastName(lastName);
        }
        if (email.length() > 0) {
            info += "Email changed to: (new)" + email + " from (old)" + user.getEmail();

            user.setEmail(email);
        }
        if (mobileNumber.length() > 0) {
            info += "Mobile Number changed to: (new)" + mobileNumber + " from (old)" + user.getMobileNumber();
            user.setMobileNumber(mobileNumber);
        }

        if (password.length() > 0) {
            String encryptedOldPassword = passwordEncryptor.passwordEncryption(oldPassword);
            if (encryptedOldPassword.equals(user.getPassword())) {
                info += "Password changed to: (new)" + password + " from (old)"  + oldPassword;
                user.setPassword(passwordEncryptor.passwordEncryption(password));
            } else {

                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Wrong old password");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
                return "account";
            }

        }
        if (roleString != null) {
            info += "1";

            if (userServiceEJB.userHasRight(user, RightType.USER_MANAGEMENT)) {
                userServiceEJB.addRole(RoleType.valueOf(roleString), user);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Rights", "Required right: " + "USER_MANAGEMENT");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }

        }
        if (deleteRoleString != null) {
            info += "1";
            if (userServiceEJB.userHasRight(user, RightType.USER_MANAGEMENT)) {

                Role role1 = null;
                boolean contains = false;
                Collection<Role> roles = user.getUserRoles();
                for (Role role : roles) {
                    if (role.getRoleString().equals(deleteRoleString)) {
                        role1 = role;
                        contains = true;
                    }
                }
                if (contains) {
                    roles.remove(role1);
                }

                user.setUserRoles(roles);


            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Rights", "Required right: " + "USER_MANAGEMENT");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }


        }

        if (info.length() > 0) {
            userServiceEJB.update(user);
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            notification.setInfo(info);
            notificationServiceEJB.save(notification);
            user.getNotifications().add(notification);
        }
        return "account";
    }


}
