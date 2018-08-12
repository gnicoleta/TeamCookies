package group.msg.jsf_beans;

import group.msg.entities.Notification;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class NotificationServiceEJB {
    @PersistenceContext
    private EntityManager em;

    public void save(Notification notification) {
        em.persist(notification);
    }

    public Notification findNotificationByRtype(String type){
        Query q = em.createNamedQuery("Role.findByRoleType", Notification.class);
        q.setParameter(1, type);
        Notification result = (Notification) q.getSingleResult();
        return result;
    }
}
