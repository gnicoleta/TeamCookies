package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.RightsForRoleGetterAndSetter;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.*;
import lombok.Data;
import org.jboss.weld.context.ejb.Ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Named
@ViewScoped
public class RegisterUserBean implements Serializable {


    @Inject
    PasswordEncryptor passwordEncryptor;

    @Inject
    UsernameGenerator usernameGenerator;

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String mobileNumber;
    private ArrayList<String> selectedRolesStrings;
    private LinkedList<Role> selectedRoles = new LinkedList<>();


    private User user;
    private List<User> users;

    @Inject
    RightsForRoleGetterAndSetter rightsForRoleGetterAndSetter;

    @EJB
    private UserServiceEJB service;

    @EJB
    private  NotificationServiceEJB notificationServiceEJB;

    @EJB
    private RoleServiceEJB roleServiceEJB;

    @Ejb
    private RightServiceEJB rightServiceEJB;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public String getRegisterInfo(String username) {
        String temp = "Welcome " + firstName + " " + lastName + "\n";
        temp += "Username=" + username + "\n";
        temp += "Email=" + email + "\n";
        temp += "Mobile number=" + mobileNumber + "\n";
        temp += "Roles: ";
        for (String s : selectedRolesStrings) {
            temp += s + " ";
        }
        temp += "\n";
        return temp;

    }

    public String registerUser() {


        String notificationInfo = "";
        service.clear();
        User user1 = new User();
        user1.setFirstName(firstName);
        user1.setLastName(lastName);
        user1.setUsername(service.generateUsername(firstName, lastName));
        if(email.endsWith("@msggroup.com")&&email!=null) {
            user1.setEmail(email);
        }
        else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Wrong email (not @msg.com)");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "register";
        }

        if(mobileNumber.startsWith("+40")||mobileNumber.startsWith("+49")&&mobileNumber.length()==12) {
            user1.setMobileNumber(mobileNumber);
        }
        else{
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Wrong mobile number not german or romanian ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "register";
        }
        user1.setPassword(passwordEncryptor.passwordEncryption(password));


        for (String roleString : selectedRolesStrings) {

            Role role = roleServiceEJB.findRoleByType(roleString);

            List<RightType> rightTypes;
            rightTypes = rightsForRoleGetterAndSetter.getRights(RoleType.valueOf(roleString));

            List<Rights> rightList = new LinkedList<>();
            Rights right;
            for (RightType rightType : rightTypes) {

                right=service.findRightByType(rightType.toString());


                rightList.add(right);

            }

            role.setRoleRights(rightList);


            selectedRoles.add(role);


        }


        Notification notification = new Notification(NotificationType.WELCOME_NEW_USER);
        notificationInfo = this.getRegisterInfo(user1.getUsername());
        notification.setInfo(notificationInfo);

        List<Notification> notifications = new LinkedList<>();

        notificationServiceEJB.save(notification);
        notifications.add(notification);

        user1.setNotifications(notifications);


        user1.setUserRoles(selectedRoles);

        service.save(user1);

        return "register";


    }


}