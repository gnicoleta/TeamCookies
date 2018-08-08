package group.msg.entities;

import group.msg.jsf_beans.UserServiceEJB;
import lombok.Data;
import org.primefaces.event.SelectEvent;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BugBean {
    @EJB
    private UserServiceEJB service;
    @PersistenceContext
    private EntityManager em;

    private Integer id;
    private String title;
    private String description;
    private String version;
    private String fixedInVersion;
    private LocalDateTime targetDate;
    private SeverityType severityType;
    private User createdBy;
    private User assignedTo;
    private StatusType statusType = StatusType.NEW;
    private Attachment attachment;
    private Notification notification;

    private String onCLickMsg = "";

    private Bug selectedBug;

    private List<Bug> bugList;

    public BugBean() {
        Query q = em.createQuery("select e from Bug e order by e.id desc ");
        List<Bug> bugList = (List) q.getResultList();

    }

    public void setSelectedBug(Bug selectedBug) {
        this.selectedBug = selectedBug;
    }

    public String rowSelected(SelectEvent event) {
        return "bugEdit";
    }


}
