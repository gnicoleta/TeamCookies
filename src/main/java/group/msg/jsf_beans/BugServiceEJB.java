package group.msg.jsf_beans;

import group.msg.beans.UsernameGenerator;
import group.msg.entities.Bug;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class BugServiceEJB {

    @PersistenceContext
    private EntityManager em;

    public void save(Bug bug) {
        em.persist(bug);
    }
}
