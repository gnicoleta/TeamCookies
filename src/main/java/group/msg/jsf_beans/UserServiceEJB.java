package group.msg.jsf_beans;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.Notification;
import group.msg.entities.Role;
import group.msg.entities.User;

import javax.ejb.Stateless;
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

    public String generateUsername(String firstname,String lastName){
        return usernameGenerator.generateUsername(firstname,lastName,em);
    }

    public User find(Integer id) {
        return em.find(User.class, id);
    }

    public void save(User user) {
        em.persist(user);

    }
    public void save(Role role){
        em.persist(role);
    }
    public void save(Notification notification){
        em.persist(notification);
    }


    public void update(User user) {
        em.merge(user);
    }

    public void delete(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }

    public boolean findUserByUsername(String username) {
        Query q = em.createNamedQuery("User.findByUsername", String.class);
        q.setParameter(1, username);
        List<String> result = q.getResultList();

        if (result.isEmpty()) {
            return false;
        } else if (result.get(0).equals(username)) {
            return true;
        } else {
            return false;
        }

    }

}