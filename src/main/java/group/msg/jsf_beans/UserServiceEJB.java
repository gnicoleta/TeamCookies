package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.Bug;
import group.msg.entities.Notification;
import group.msg.entities.Role;
import group.msg.entities.User;

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


    public void update(User user) {
        em.merge(user);
    }

    public void delete(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
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

    public User getUserByUsername(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();


        return result;
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

    public String ifExistsDelete(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();
        if (result != null) {
            result.setUserStatus(false);
            this.update(result);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User status: inactive", "User deleted."));
            return "";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User not found", "User not found."));
            return "";
        }
    }

    public void ifExistsActivate(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();
        if (result != null) {
            result.setUserStatus(true);
            this.update(result);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User status: active", "User activated."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User not found", "User not found."));
        }
    }


}