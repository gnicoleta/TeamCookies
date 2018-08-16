package group.msg.jsf_beans;

import group.msg.entities.Attachment;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.logging.Logger;

@Stateless
public class AttachmentServiceEJB {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Logger logger;

    public void save(Attachment attachment) {
        try {
            em.persist(attachment);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void delete(Attachment attachment) {
        try {
            em.remove(em.contains(attachment) ? attachment : em.merge(attachment));
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }
}
