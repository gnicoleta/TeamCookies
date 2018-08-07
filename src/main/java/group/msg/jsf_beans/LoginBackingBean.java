package group.msg.jsf_beans;

import group.msg.entities.User;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Data
@Named
@SessionScoped
public class LoginBackingBean implements Serializable {
    //private String username;
    private int username;
    private String pwd;

   // @Inject
    private UserDBOperationsBean user;

    //@Inject
    private EntityManager em;



    public String validateUsernamePassword() {
        //user.startDB(0);
        //Query query = em.createQuery("SELECT sp user (USERNAME, FIRST_NAME) VALUES (?, ?)", User.class);
       // query.setParameter(1, "ana");
        //query.setParameter(2, "mere");
       // query.executeUpdate();

        TypedQuery<User> query = em.createQuery("SELECT e FROM User e WHERE e.username LIKE 'ana'", User.class);
        List<User> list = query.getResultList();

        System.out.println("EX1");
        for (User i : list) {
            System.out.println("Student: " + i.getId() + " is from city: "+ i.getUsername() );
        }

        User user1 = new User();
        user1.setLastName("llllllll");
        em.persist(user1);


        user.insertUser("ana", "maria");
        if (user.findId(username) && pwd.equals("admin")) {
            WebHelper.getSession().setAttribute("loggedIn",true);
            return "homepage";
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "Invalid credentials."));
            return "";
        }
    }

    public String getCurrentlyLoggedInUsername(){
        return "aaa";
    }
}
