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

    public Integer save(User book) {
        em.persist(book);
        return book.getId();
    }

    public void update(User book) {
        em.merge(book);
    }

    public void delete(User book) {
        em.remove(em.contains(book) ? book : em.merge(book));
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