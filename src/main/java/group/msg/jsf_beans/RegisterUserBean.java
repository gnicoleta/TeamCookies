package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.*;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@Named
@SessionScoped
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
    private LinkedList<Role> seletectedRoles=new LinkedList<>();

    private User user;
    private List<User> users;

    @EJB
    private UserServiceEJB service;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public String registerUser() {


        User user1 = new User();
        user1.setFirstName(firstName);
        user1.setLastName(lastName);
        user1.setUsername(service.generateUsername(firstName, lastName));
        user1.setEmail(email);
        user1.setMobileNumber(mobileNumber);
        user1.setPassword(passwordEncryptor.passwordEncryption(password));
        for(String roleString:selectedRolesStrings){
            Role role=new Role(RoleType.valueOf(roleString));
            seletectedRoles.add(role);
            service.save(role);

        }
        Notification notification=new Notification(NotificationType.WELCOME_NEW_USER);
        service.save(notification);

        user1.setUserRoles(seletectedRoles);

        service.save(user1);

        return "register";


    }


}
