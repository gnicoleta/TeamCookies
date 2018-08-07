package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.Role;
import group.msg.entities.RoleType;
import group.msg.entities.User;
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

//    @Inject
//    PasswordEncryptor passwordEncryptor;
//
//    @Inject
//    UsernameGenerator usernameGenerator;
//
//    @EJB
//    UserServiceEJB serviceEJB;
//
//    private String firstName;
//    private String lastName;
//
//    private String email;
//    private String password;
//
//    private String mobileNumber;
//
//    private LinkedList<String> selectedRolesStrings;
//
//    private LinkedList<Role> selectedRoles;
//
//
//    private User user;
//    private List<User> users;
//
//    @PostConstruct
//    public void init() {
//        user = new User();
//    }
//
//    public String registerUser() {
//
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//
//        serviceEJB.save(user);
//
//        return "homepage";
//
//    }

    private String firstName;
    private String lastName;
    private User user;
    private List<User> users;

    @EJB
    private UserServiceEJB service;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public String registerUser() {

        //for the moment the db is empty so to check if my method works, I'm adding a user here
        User user1 = new User();
        user1.setFirstName(firstName);
        user1.setLastName(lastName);
        service.save(user1);

        return "homepage";

//        if ( service.findUserByUsername(username)) {
//            WebHelper.getSession().setAttribute("loggedIn",true);
//            return "homepage";
//        }else{
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "Invalid credentials."));
//            return "";
//        }
    }



}
