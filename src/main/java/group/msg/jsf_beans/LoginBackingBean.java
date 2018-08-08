package group.msg.jsf_beans;
//import group.msg.test.jpa.JPABaseTest;

import group.msg.entities.User;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;

@Data
@Named
@SessionScoped
public class LoginBackingBean implements Serializable {
    private String username;
    private String pwd;

    private User user;
    private List<User> users;

    @EJB
    private UserServiceEJB service;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public String validateUsernamePassword() {

        //for the moment the db is empty so to check if my method works, I'm adding a user here
        User user1 = new User();
        user1.setUsername("admin");
        service.save(user1);

        if (service.findUserByUsername(username) && pwd.equals("admin")) {
            WebHelper.getSession().setAttribute("loggedIn", true);
            return "homepage";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "Invalid credentials."));
            return "";
        }
    }

    public String getCurrentlyLoggedInUsername() {
        return user.getUsername();
    }
}
