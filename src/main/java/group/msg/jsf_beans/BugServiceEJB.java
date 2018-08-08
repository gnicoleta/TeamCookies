package group.msg.jsf_beans;

import group.msg.beans.UsernameGenerator;
import group.msg.entities.Bug;
import group.msg.entities.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class BugServiceEJB {

    @PersistenceContext
    private EntityManager em;

    public void save(Bug bug) {
        em.persist(bug);
    }

}
