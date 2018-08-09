package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.*;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class UserServiceEJB {


    @Inject
    PasswordEncryptor passwordEncryptor;

    @Inject
    UsernameGenerator usernameGenerator;

    @PersistenceContext
    private EntityManager em;

    public String generateUsername(String firstname, String lastName) {
        return usernameGenerator.generateUsername(firstname, lastName, em);
    }

    public User find(Integer id) {
        return em.find(User.class, id);
    }

    public void save(User user) {
        em.persist(user);

    }

    public void save(Role role) {
        em.persist(role);
    }

    public void save(Notification notification) {
        em.persist(notification);
    }

    public void clear() {
        em.clear();
    }

    public void update(User user) {
        em.merge(user);
    }

    public void updateBug(Bug bug) {
        em.merge(bug);
    }

    public void delete(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }

    public User getUserByUsername(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();
        return result;
    }

    public boolean findUserByUsername(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        List<User> result = q.getResultList();
        if (result.isEmpty()) {
            return false;
        } else if (result.get(0).getUsername().equals(username)) {
            return true;
        } else {
            return false;
        }

    }

    public List<User> getAllUsers() {
        Query q = em.createNamedQuery("User.findAll", User.class);
        List<User> result = q.getResultList();
        return result;
    }

    public List<Bug> getAllBugs() {
        Query q = em.createNamedQuery("Bug.findAll", Bug.class);
        List<Bug> result = q.getResultList();
        return result;
    }

    public boolean ifExistsDelete(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();
        if (result != null) {
            result.setUserStatus(UserStatus.INACTIVE);
            this.update(result);
            return true;
        } else {
            return false;
        }
    }

    public boolean ifExistsActivate(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();
        if (result != null) {
            result.setUserStatus(UserStatus.ACTIVE);
            this.update(result);
            return true;
        } else {
            return false;
        }
    }
    /*
    public void editUser(String userName){
        Query q = em.createQuery("select u from User u where u.username like ?1");
        q.setParameter(1,userName);
        User result =(User) q.getSingleResult();
        if (this.findUserByUsername(userName)) {

        }
    }
    */


}