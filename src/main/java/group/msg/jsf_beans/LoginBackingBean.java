package group.msg.jsf_beans;
//import group.msg.test.jpa.JPABaseTest;

import group.msg.beans.PasswordEncryptor;
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

    @Inject
    PasswordEncryptor passwordEncryptor;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public String validateUsernamePassword() {

        User userAdmin;
        boolean userPresentInDB = true;
        User user1 = null;

        
        if (username.equals("admin") && pwd.equals("admin")) {

            userAdmin=new User();
            userAdmin.setUsername(username);
            userAdmin.setPassword(pwd);
            service.save(userAdmin);
            return "homepage";
        } else {


            try {


                user1 = service.getUserByUsername(username);
            } catch (Exception e) {
                userPresentInDB = false;
            }


            String encryptedInputPassword = passwordEncryptor.passwordEncryption(pwd);


            if (userPresentInDB && encryptedInputPassword.equals(user1.getPassword())) {
                WebHelper.getSession().setAttribute("loggedIn", true);
                //WebHelper.getSession().setAttribute("currentUser",user1);
                return "homepage";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "Invalid credentials."));
                return "";
            }
        }
    }

    public String getCurrentlyLoggedInUsername() {
        return user.getUsername();
    }
}
