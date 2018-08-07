package group.msg.jsf_beans;

import group.msg.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class T1 {

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

}