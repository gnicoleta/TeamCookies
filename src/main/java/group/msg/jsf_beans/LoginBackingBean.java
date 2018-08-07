package group.msg.jsf_beans;
//import group.msg.test.jpa.JPABaseTest;

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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;

@Data
@Named
@SessionScoped
public class LoginBackingBean  implements Serializable {
    //private String username;
    private int username;
    private String pwd;

    //@Inject
    protected UserTransaction utx;

   // @Inject
    private UserDBOperationsBean user;

    @PersistenceContext
    EntityManager em;


    private User book;
    private List<User> books;

    @EJB
    private T1 service;

    @PostConstruct
    public void init() {
        book = new User();
        //books = service.list();
    }



    public String validateUsernamePassword() {
        //user.startDB(0);
        //Query query = em.createQuery("SELECT sp user (USERNAME, FIRST_NAME) VALUES (?, ?)", User.class);
       // query.setParameter(1, "ana");
        //query.setParameter(2, "mere");
       // query.executeUpdate();

//        //em.joinTransaction();
//        try {
//            utx.begin();
//        } catch (NotSupportedException e) {
//            e.printStackTrace();
//        } catch (SystemException e) {
//            e.printStackTrace();
//        }
//        em.joinTransaction();



        User user1 = new User();
        user1.setFirstName("llllllll");

        service.save(user1);

        em.persist(user1);

        Query query = em.createQuery("SELECT e FROM User e WHERE e.firstName LIKE 'llllllll'");
        List<User> list = (List<User>) query.getResultList();

        System.out.println("EX1");
        for (User i : list) {
            System.out.println("USER: " + i.getId() + " has username: "+ i.getUsername() );
        }




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
