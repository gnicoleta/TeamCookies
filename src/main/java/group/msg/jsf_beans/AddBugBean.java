package group.msg.jsf_beans;

import group.msg.entities.*;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.ConstraintValidatorContext;
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

    @EJB
    NotificationServiceEJB notificationServiceEJB;
    private String title;
    private String description;
    private String version;
    private String severityTypeString;
    private Date targetDate;
    private SeverityType severityType;
    private String username;
    private User assignedTo;

    private Attachment attachment;
    private Notification notification;

    private Bug bug;

    public String addBug() {
        notification = new Notification();
        bug = new Bug();
        bug.setTitle(title);
        if(isValidDescription()) {
            bug.setDescription(description);
        }
        else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Wrong Description input", "Input must be at least 10 characters in length.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return "AddBug";
        }

        if (isValidVersion(version)) {
            bug.setVersion(version);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Wrong Version input", "Input must be alfanumerical separated by dot!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return "AddBug";
        }
        bug.setTargetDate(targetDate);

        SeverityType severityType = SeverityType.valueOf(severityTypeString);
        bug.setSeverityType(severityType);

        User user = null;
        try {
            user = userServiceEJB.getUserByUsername(username);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Assigned user does not exist!!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return "AddBug";
        }
        bug.setCreatedBy((User) WebHelper.getSession().getAttribute("currentUser"));
        bug.setAssignedTo(user);
        String data = "NEW BUG!   " + "\n Title:" + title + ". Description:" + description + ". Version:" + version + ". Target date:" + targetDate + ". Severity type:" + severityTypeString;
        notification.setInfo(data);
        notification.setNotificationType(NotificationType.BUG_UPDATED);
        bugServiceEJB.save(bug);
        notification.setBugId(bug.getId());
        notificationServiceEJB.save(notification);
        user.getNotifications().add(notification);
        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(notification);
        notificationServiceEJB.update(notification);
        return "bugManagement";
    }

    public boolean isValidVersion(String descr) {

        if (null == descr) {
            return false;
        }
        int flag = 1;
        char[] charArray = descr.toCharArray();
        int size = charArray.length;
        for (int i = 0; i < size; i++) {
            if (!(Character.isLetterOrDigit(charArray[i]) || charArray[i]=='.')) {
                flag = 0;
            }
        }
        if (!(contains(charArray, '.') && charArray[0] != '.' && charArray[size - 1] != '.'))
            flag = 0;
        if (flag == 1)
            return true;
        else
            return false;
    }
    public boolean contains(char[] charArr, char chr) {
        String str = String.valueOf(charArr);
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == chr)
                return true;
        return false;
    }

    public boolean isValidDescription(){
        if(description.length()>=10)
            return true;
        else
            return false;
    }
}
