package group.msg.jsf_beans;

import group.msg.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserServiceEJB {

    @PersistenceContext
    private EntityManager em;

    public User find(Integer id) {
        return em.find(User.class, id);
    }

    public Integer save(User user) {
        em.persist(user);
        return user.getId();
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