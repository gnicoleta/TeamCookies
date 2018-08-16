package group.msg.jsf_beans;


import group.msg.entities.Role;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.logging.Logger;

@Stateless
public class RoleServiceEJB {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private RightServiceEJB rightServiceEJB;

    @Inject
    private Logger logger;

    public void save(Role role) {
        try {
            em.persist(role);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void update(Role role) {
        try {
            em.merge(role);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public Role findRoleByType(String type) {
        try {
            try {
                Query q = em.createNamedQuery("Role.findByRoleType", Role.class);
                q.setParameter(1, type);
                Role result = (Role) q.getSingleResult();
                return result;
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (NoResultException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }
}
