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
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

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

    @EJB
    AttachmentServiceEJB attachmentServiceEJB;


    private String title;
    private String description;
    private String version;
    private String severityTypeString;
    private Date targetDate;
    private SeverityType severityType;
    private String username;

    private Attachment attachment;

    //private User createdBy;
    private User assignedTo;

    // private Attachment attachment;
    private Notification notification = new Notification();

    private Bug bug;

    @Inject
    private Logger logger;

    public String addBug() {
        try {
            bug = new Bug();
            bug.setTitle(title);
            bug.setDescription(description);
            bug.setVersion(version);

            bug.setTargetDate(targetDate);

            SeverityType severityType = SeverityType.valueOf(severityTypeString);
            bug.setSeverityType(severityType);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        User user = null;
        try {
            user = userServiceEJB.getUserByUsername(username);
        } catch (Exception e) {
            logger.info(Arrays.toString(e.getStackTrace()));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Assigned user does not exist!!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return "AddBug";
        }
        try {
            bug.setCreatedBy((User) WebHelper.getSession().getAttribute("currentUser"));
            bug.setAssignedTo(user);
            String data = "NEW BUG!   " + "\n Title:" + title + ". Description:" + description + ". Version:" + version + ". Target date:" + targetDate + ". Severity type:" + severityTypeString;
            notification.setInfo(data);
            notification.setNotificationType(NotificationType.BUG_UPDATED);
            notificationServiceEJB.save(notification);
            user.getNotifications().add(notification);
            if (attachment != null) {
                attachmentServiceEJB.save(attachment);
                bug.setAttachment(attachment);
            }
        }catch (Exception e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(notification);
        bugServiceEJB.save(bug);
        return "bugManagement";
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            try {
                byte[] b = event.getFile().getContents();
                attachment = new Attachment();
                attachment.setAttachmentByte(b);
                attachment.setAttachmentType(event.getFile().getContentType());
                attachment.setExtensionType(FilenameUtils.getExtension(event.getFile().getFileName()));
                FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (Exception e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

    }


}
