package group.msg.jsf_beans;

import group.msg.entities.Attachment;
import group.msg.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AttachmentServiceEJB {

    @PersistenceContext
    private EntityManager em;


    public void delete(Attachment attachment) {
        em.remove(em.contains(attachment) ? attachment : em.merge(attachment));
    }
}
