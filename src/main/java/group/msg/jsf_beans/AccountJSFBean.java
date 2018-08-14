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
    private String addRightRole;
    private String addRight;
    private String deleteRightRole;
    private String deleteRight;
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

        String info ="";

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
        if (roleString != null) {
        info+="1";

            if (userServiceEJB.userHasRight(user, RightType.USER_MANAGEMENT)) {
                userServiceEJB.addRole(RoleType.valueOf(roleString), user);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Rights", "Required right: " + "USER_MANAGEMENT");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }

        }
        if (deleteRoleString != null) {
            info+="1";
            if (userServiceEJB.userHasRight(user, RightType.USER_MANAGEMENT)) {

                Role role1 = null;
                boolean contains=false;
                Collection<Role> roles = user.getUserRoles();
                for (Role role : roles) {
                    if (role.getRoleString().equals(deleteRoleString)) {
                        role1 = role;
                        contains=true;
                    }
                }
                if(contains){
                    roles.remove(role1);
                }

                user.setUserRoles(roles);


            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Rights", "Required right: " + "USER_MANAGEMENT");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }


        }

        if (addRightRole != null) {
            if(addRight!=null){
                Role role=roleServiceEJB.findRoleByType(addRightRole);
                Collection<Rights> rights=role.getRoleRights();
                rights.add(rightServiceEJB.findRightByType(addRight));
                role.setRoleRights(rights);
                roleServiceEJB.update(role);
                info+=role.getRoleString()+"---"+addRight;


            }


        }

        if (deleteRightRole != null) {
            if(deleteRight!=null){
                Role role=roleServiceEJB.findRoleByType(deleteRightRole);
                Collection<Rights> rights=role.getRoleRights();
                Rights rights1=null;
                boolean contains=false;
                
                for(Rights right:rights){
                    if(right.getTypeString().equals(RightType.valueOf(deleteRight))){
                        rights1=right;
                        contains=true;
                        info+="kdosakdos";
                    }
                }
                if(contains){
                    rights.remove(rights1);
                }
                role.setRoleRights(rights);
                roleServiceEJB.update(role);
                info+=role.getRoleString()+"---"+deleteRight;


            }


        }
        if(info.length()>0) {
            userServiceEJB.update(user);
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            notification.setInfo(info);
            notificationServiceEJB.save(notification);
            user.getNotifications().add(notification);
        }
        return "account";
    }


}
