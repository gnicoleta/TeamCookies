package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.RightsForRoleGetterAndSetter;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.*;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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
    private LinkedList<String> selectedRolesStrings;
    private LinkedList<Role> selectedRoles = new LinkedList<>();


    private User user;
    private List<User> users;

    @Inject
    RightsForRoleGetterAndSetter rightsForRoleGetterAndSetter;

    @EJB
    private UserServiceEJB service;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public String registerUser() {


        service.clear();
        User user1 = new User();
        user1.setFirstName(firstName);
        user1.setLastName(lastName);
        user1.setUsername(service.generateUsername(firstName, lastName));
        user1.setEmail(email);
        user1.setMobileNumber(mobileNumber);
        user1.setPassword(passwordEncryptor.passwordEncryption(password));


        for (String roleString : selectedRolesStrings) {
            Role role = new Role(RoleType.valueOf(roleString));

            List<RightType> rightTypes = new ArrayList<>();
            rightTypes = rightsForRoleGetterAndSetter.getRights(RoleType.valueOf(roleString));

            List<Right> rightList = new LinkedList<>();
            for (RightType rightType : rightTypes) {


                Right right = new Right(rightType);
                rightList.add(right);
                service.save(right);
            }

            role.setRoleRights(rightList);


            selectedRoles.add(role);
            service.save(role);

        }
        Notification notification = new Notification(NotificationType.WELCOME_NEW_USER);
        List<Notification>notifications=new LinkedList<>();

        service.save(notification);
        notifications.add(notification);

        user1.setNotifications(notifications);




        user1.setUserRoles(selectedRoles);

        service.save(user1);

        return "register";


    }


}
