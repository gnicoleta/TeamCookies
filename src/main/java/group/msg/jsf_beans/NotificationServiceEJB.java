package group.msg.jsf_beans;

import group.msg.entities.Bug;
import group.msg.entities.Notification;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.logging.Logger;

@Stateless
public class NotificationServiceEJB {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Logger logger;

    public void save(Notification notification) {
        try {
            em.persist(notification);
        }catch (NullPointerException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void update(Notification notification) {
        try {
            em.merge(notification);
        }catch (NullPointerException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public Notification findNotificationByRtype(String type){
        try {
            Query q = em.createNamedQuery("Role.findByRoleType", Notification.class);
            q.setParameter(1, type);
            Notification result = (Notification) q.getSingleResult();
            return result;
        }catch (NullPointerException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }
}
