package group.msg.jsf_beans;
import group.msg.entities.*;
import lombok.Data;
import org.primefaces.context.RequestContext;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
@Data
@Named
@ViewScoped
public class AddBugBean implements Serializable {


    @EJB
    BugServiceEJB bugServiceEJB;

    @EJB
    UserServiceEJB userServiceEJB;


    private String title;
    private String description;
    private String version;
    private String severityTypeString;
    private Date targetDate;
    private SeverityType severityType;
    private String username;



    //private User createdBy;
    private User assignedTo;

    // private Attachment attachment;
    private Notification notification;

    private Bug bug;

    public String addBug() {

        bug = new Bug();
        bug.setTitle(title);
        bug.setDescription(description);
        bug.setVersion(version);

        bug.setTargetDate(targetDate);

        SeverityType severityType=SeverityType.valueOf(severityTypeString);
        bug.setSeverityType(severityType);

        User user=null;
        try {
          user = userServiceEJB.getUserByUsername(username);
        }catch (Exception e){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Assigned user does not exist!!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return "AddBug";
        }
        bug.setCreatedBy((User)WebHelper.getSession().getAttribute("currentUser"));


        bug.setAssignedTo(user);

        bugServiceEJB.save(bug);

        return "bugManagement";
    }

}
