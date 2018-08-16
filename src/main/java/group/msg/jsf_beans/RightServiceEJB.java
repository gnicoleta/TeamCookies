package group.msg.jsf_beans;


import group.msg.entities.Rights;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.logging.Logger;

@Stateless
public class RightServiceEJB {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Logger logger;

    public void update(Rights right) {
        try {
            em.merge(right);
        }catch (NullPointerException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void save(Rights right) {
        try {
            em.persist(right);
        }catch (NullPointerException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    @SuppressWarnings("Duplicates")
    public Rights findRightByType(String type) {
        try {
            Query q = em.createNamedQuery("Rights.findByRightType", Rights.class);
            q.setParameter(1, type);
            Rights result = (Rights) q.getSingleResult();
            return result;
        }catch (NullPointerException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }
}
