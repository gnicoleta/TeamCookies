package group.msg.jsf_beans;

import java.io.*;
import java.util.List;

import group.msg.entities.*;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.*;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@Named
@ViewScoped
@Data
public class BugBean extends LazyDataModel<Bug> implements Serializable {
    @EJB
    private UserServiceEJB service;

    @EJB
    private BugServiceEJB bugService;

    @EJB
    private NotificationServiceEJB notificationServiceEJB;

    @EJB
    private AttachmentServiceEJB attachmentServiceEJB;

    @Inject
    private DownloadBean downloadBean;

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

    private String onCLickMsg = "nume";

    private Bug selectedBug;

    private List<Bug> bugList;

    private List<Bug> selectedBugs;


    @PostConstruct
    public void init() {
        bugList = bugService.getAllBugs();
    }

    @Override
    public Bug getRowData(String rowKey) {
        Integer id = Integer.parseInt(rowKey);
        return bugList.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
    }

    @Override
    public Object getRowKey(Bug object) {
        return object.getId();
    }

    @Override
    public java.util.List<Bug> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        java.util.List<Bug> filteredList = new ArrayList<>();
        bugList.forEach(bug -> {
            boolean match = true;
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext(); ) {

                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);

                        Field fieldToFilter = null;
                        for (Field field : Bug.class.getDeclaredFields()) {
                            if (field.getName().equals(filterProperty)) {
                                fieldToFilter = field;
                            }
                        }

                        if (fieldToFilter != null) {
                            fieldToFilter.setAccessible(true);
                        } else {
                            continue;
                        }

                        String fieldValue = String.valueOf(fieldToFilter.get(bug));

                        if (filterValue.equals("") || filterValue == null || fieldValue.contains(filterValue.toString())) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                    } catch (Exception e) {
                        match = false;
                    }
                }
            }
            if (match) {
                filteredList.add(bug);
            }
        });


        int dataSize = filteredList.size();
        if (sortField != null) {
            filteredList.sort(new BugSorter(sortField, sortOrder));
        }
        this.setRowCount(dataSize);

//        paginate

        if (dataSize > pageSize) {
            try {
                return filteredList.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return filteredList.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return filteredList;
        }
    }

    public static class BugSorter implements Comparator<Bug> {
        private String sortField;
        private SortOrder sortOrder;

        public BugSorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(Bug bug, Bug t1) {
            try {
                Field fieldToSort = null;
                for (Field field : Bug.class.getDeclaredFields()) {
                    if (field.getName().equals(sortField)) {
                        fieldToSort = field;
                    }
                }

                if (fieldToSort != null) {
                    fieldToSort.setAccessible(true);
                } else {
                    return 1;
                }

                Object val1 = fieldToSort.get(bug);
                Object val2 = fieldToSort.get(t1);

                int comparisionResult = ((Comparable) val1).compareTo(val2);

                return SortOrder.ASCENDING.equals(sortOrder) ? comparisionResult : (-1) * comparisionResult;
            } catch (Exception e) {
                return 1;
            }
        }
    }

    public Bug getSelectedBug() {
        return selectedBug;
    }

    public void setSelectedBug(Bug selectedBug) {
        this.selectedBug = selectedBug;
        bugService.update(selectedBug);
    }


    public File byteToFile(byte[] byteFile, String filename) {
        File file = new File(filename);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(byteFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;

    }


    public void handleFileUpload(FileUploadEvent event) {
        byte[] b = event.getFile().getContents();
        Attachment attachment = new Attachment();
        attachment.setAttachmentByte(b);
        attachment.setAttachmentType(event.getFile().getContentType());
        attachment.setExtensionType(FilenameUtils.getExtension(event.getFile().getFileName()));
        selectedBugs.get(0).setAttachment(attachment);
        bugService.save(selectedBugs.get(0));
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void deleteAttachment() {
        try {
            Attachment attachment = selectedBugs.get(0).getAttachment();
            selectedBugs.get(0).setAttachment(null);
            attachmentServiceEJB.delete(attachment);
        }catch (Exception e){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Missing attachment");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public StreamedContent downloadAttachment() throws IOException {
        Attachment attachment=null;
        try {
            attachment = selectedBugs.get(0).getAttachment();

            File file = byteToFile(attachment.getAttachmentByte(), "MyAttachment");

            InputStream stream = new FileInputStream(file.getAbsolutePath());


            String contentType=attachment.getAttachmentType();
            String extension=attachment.getExtensionType();

            return new DefaultStreamedContent(stream, contentType, "downloaded_bug_attachment."+extension);
        }catch (Exception e){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Missing attachment");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        return null;
    }


    public StreamedContent getPDF() throws IOException {
        PDFWriter pdfWriter=null;
        try {
            List<Bug> bugs = new ArrayList<>();
            bugs.add(selectedBugs.get(0));

            pdfWriter = downloadBean.getPDFWriter();

            pdfWriter.createPDF(bugs, "Bug_Info");


            return pdfWriter.getFile();
        }catch (Exception e){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Invalid bug");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

        return null;


    }

    public StreamedContent getExcel() throws IOException {

        ExcelWriter excelWriter=null;
        try {
            excelWriter = downloadBean.getExcelWriter();
            excelWriter.createExcel(selectedBugs, "Bug_Info");

            return excelWriter.downloadAttachment();
        }catch (Exception e){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Invalid bug");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }


        return null;

    }

    public void sendClosedNotification() {
        Notification closed = new Notification();
        User user = selectedBug.getAssignedTo();
        String data = "BUG CLOSED!    Title:" + selectedBug.getTitle() + ". Description:" + selectedBug.getDescription() + ". Version:" + selectedBug.getVersion() + ". Target date:" + selectedBug.getTargetDate() + ". Severity type:" + selectedBug.getSeverityType();
        closed.setInfo(data);
        closed.setNotificationType(NotificationType.BUG_CLOSED);
        user.getNotifications().add(closed);
        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(closed);
    }

    //
//    public void sendUpdatedNotification(){
//        Notification updated=new Notification();
//        String data = "BUG UPDATED!    Title:" + selectedBug.getTitle() + ". Description:" + selectedBug.getDescription() + ". Version:" + selectedBug.getVersion() + ". Target date:" + selectedBug.getTargetDate() + ". Severity type:" + selectedBug.getSeverityType();
//        updated.setInfo(data);
//        updated.setNotificationType(NotificationType.BUG_UPDATED);
//        selectedBug.getAssignedTo().getNotifications().add(updated);
//        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(updated);
//
//    }

    public void updateTitle() {
        Notification notification = new Notification(NotificationType.BUG_UPDATED);
        String data = "TITLE UPDATED!    Title:" + title + ". Description:" + selectedBug.getDescription() + ". Version:" + selectedBug.getVersion() + ". Target date:" + selectedBug.getTargetDate() + ". Severity type:" + selectedBug.getSeverityType();
        selectedBug.setTitle(title);
        notification.setInfo(data);
        notificationServiceEJB.save(notification);
        selectedBug.getAssignedTo().getNotifications().add(notification);
        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(notification);
        bugService.update(selectedBug);
    }
    public void bugStatusUpdated(){
        Notification notification = new Notification(NotificationType.BUG_STATUS_UPDATED);
        String data = "TITLE UPDATED!    Title:" + selectedBug.getTitle() + ". Description:" + selectedBug.getDescription() + ". Version:" + selectedBug.getVersion() + ". Target date:" + selectedBug.getTargetDate() + ". Severity type:" + selectedBug.getSeverityType();
        notification.setInfo(data);
        notificationServiceEJB.save(notification);
        selectedBug.getAssignedTo().getNotifications().add(notification);
        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(notification);
        bugService.update(selectedBug);
    }
}