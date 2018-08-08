package group.msg.jsf_beans;


import group.msg.beans.PasswordEncryptor;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.*;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Named
@SessionScoped
public class AddBugBean implements Serializable {


    @EJB
    BugServiceEJB bugServiceEJB;

    private String title;
    private String description;
    private String version;

    private Date targetDate;
    private SeverityType severityType;
    //private User createdBy;
    // private User assignedTo;

    // private Attachment attachment;
    private Notification notification;

    private Bug bug;

    public String addBug() {

        bug = new Bug();
        bug.setTitle(title);
        bug.setDescription(description);
        bug.setVersion(version);

        bug.setTargetDate(targetDate);

        bugServiceEJB.save(bug);
        return "bugManagement";
    }

}
