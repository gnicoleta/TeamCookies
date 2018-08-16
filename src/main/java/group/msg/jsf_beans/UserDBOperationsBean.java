package group.msg.jsf_beans;

import group.msg.entities.User;
import lombok.Data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.logging.Logger;

@Data
@Stateless
public class UserDBOperationsBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Logger logger;

    public void startDB(int id) {
        try {

            em.find(User.class, id);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void insertUser(String firstName, String lastName) {

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        logger.info("User added: " + user.getFirstName() + " " + user.getLastName());
        em.persist(user);
        em.flush();
    }

    public boolean findId(int id) {
        if (em.find(User.class, id) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void updateUser(int userId, String firstName, String lastName) {
        User user = em.find(User.class, userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        logger.info("User updated: id: " + user.getId() + System.lineSeparator() + user.getFirstName() + " " + user.getFirstName());
    }

    public void deleteUser(int userId) {
        User user = em.find(User.class, userId);
        em.remove(user);
        logger.info("User deleted: id: " + user.getId() + System.lineSeparator() + user.getFirstName() + " " + user.getFirstName());
    }

    public boolean findUserByUsername(String username) {
        String result = null;
        try {
            Query q = em.createNamedQuery("User.findByUsername", String.class);
            q.setParameter(1, username);
            result = (String) q.getSingleResult();
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        if (result.isEmpty()) {
            return false;
        } else if (result.equals(username)) {
            return true;
        } else {
            return false;
        }

    }
}