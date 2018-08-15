package group.msg.jsf_beans;


import group.msg.entities.Bug;


import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Stateless
public class BugServiceEJB {

    @PersistenceContext
    private EntityManager em;

    public void save(Bug bug) {
        em.persist(bug);
    }

    public void update(Bug bug) {
        em.merge(bug);
    }

    public List<Bug> getAllBugs() {
        Query q = em.createNamedQuery("Bug.findAll", Bug.class);
        List<Bug> result = q.getResultList();
        return result;
    }
    public Bug findBugByTitle(String bugTitle) {
        Query q = em.createNamedQuery("Bug.findBugByTitle");
        q.setParameter(1, bugTitle);
        return (Bug) q.getSingleResult();
    }

}
