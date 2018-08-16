package group.msg.jsf_beans;


import group.msg.entities.Bug;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class BugServiceEJB {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Logger logger;

    public void save(Bug bug) {
        try {
            em.persist(bug);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void update(Bug bug) {
        try {
            em.merge(bug);
            logger.info("Bug updated: "+bug.toString());
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public List<Bug> getAllBugs() {
        try {
            try {
                Query q = em.createNamedQuery("Bug.findAll", Bug.class);
                List<Bug> result = q.getResultList();
                return result;
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (NoResultException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    public Bug findBugByTitle(String bugTitle) {
        try {
            try {
                Query q = em.createNamedQuery("Bug.findBugByTitle");
                q.setParameter(1, bugTitle);
                return (Bug) q.getSingleResult();
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        } catch (NoResultException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }
    public Bug findBugById(int bugId) {
        try {
            try {
                Query q = em.createQuery("select b from Bug b where b.id = :id");
                q.setParameter("id", bugId);
                return (Bug) q.getSingleResult();
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (NoResultException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

}
