package group.msg.jsf_beans;

import group.msg.entities.User;
import lombok.Data;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

import static javax.ejb.TransactionAttributeType.MANDATORY;

@Data
@Stateless
public class UserDBOperationsBean {

    @PersistenceContext
    private EntityManager em;

    public void startDB(int id) {
        em.find(User.class, id);
    }

    public void insertUser(String firstName, String lastName) {

       User user = new User();
        user.setFirstName(firstName);
       user.setLastName(lastName);
       em.persist(user);
       em.flush();
    }

    public boolean findId(int id) {
        if(em.find(User.class,id)!= null) {
            return true;
        } else {
            return false;
        }
    }

    public void updateUser(int userId, String firstName, String lastName) {
        User user = em.find(User.class, userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }

    public void deleteUser(int userId) {
        User user = em.find(User.class, userId);
        em.remove(user);
    }

    public boolean findUserByUsername(String username) {
        Query q = em.createNamedQuery("User.findByUsername", String.class);
        q.setParameter(1, username);
        String result = (String) q.getSingleResult();

        if (result.isEmpty()) {
            return false;
        } else if (result.equals(username)) {
            return true;
        } else {
            return false;
        }

    }
}