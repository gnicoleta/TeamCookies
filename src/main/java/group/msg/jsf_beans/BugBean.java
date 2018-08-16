package group.msg.jsf_beans;

import group.msg.entities.*;
import group.msg.validator.bugInfoValidator.revisionValidation;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean
@Named
@ViewScoped
@Data
public class BugBean extends LazyDataModel<Bug> implements Serializable {
    @EJB
    private UserServiceEJB service;

    @Inject
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
    private String title = null;
    private String description = null;

    @revisionValidation
    private String version = null;

    private int defaultValue;


    private String fixedInVersion;
    private LocalDateTime targetDate;
    private SeverityType severityType = null;
    private User createdBy;
    private String assignedTo = null;
    private StatusType statusType = null;
    private Attachment attachment;
    private Notification notification;
    private Bug selectedBug;

    private List<Bug> bugList = new ArrayList<>();
    private List<Bug> selectedBugs=null;

    @PostConstruct
    public void init() {
        bugList = bugService.getAllBugs();
    }

    @Override
    public Bug getRowData(String rowKey) {
        try {
            Integer id = Integer.parseInt(rowKey);
            return bugList.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object getRowKey(Bug object) {
        try {
            return object.getId();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
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
                        e.printStackTrace();
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
                e.printStackTrace();
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
                e.printStackTrace();
                return 1;
            }
        }
    }

    public Bug getSelectedBug() {
        return selectedBug;
    }

    public void setSelectedBug(Bug selectedBug) {
        this.selectedBug = selectedBug;
    }

    public void rowSelected(SelectEvent event) {
        try {
            try {
                this.updateBugTitle(selectedBugs.get(0).getTitle());
                this.updateBugDescription(selectedBugs.get(0).getDescription());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void updateBugTitle(String newTitle) {
        try {
            try {
                selectedBugs.get(0).setTitle(newTitle);
                bugService.update(selectedBugs.get(0));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void updateBugDescription(String newDescription) {
        try {
            try {
                selectedBugs.get(0).setDescription(newDescription);
                bugService.update(selectedBugs.get(0));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    /**
     * Might be useful someday
     */
//    public byte[] fileToByte(File file) {
//        byte[] fileContent = null;
//        try {
//            Path path = Paths.get(file.getAbsolutePath());
//            fileContent = Files.readAllBytes(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return fileContent;
//    }
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
        try {
            try {
                try {
                    byte[] b = event.getFile().getContents();
                    Attachment attachment = new Attachment();
                    attachment.setAttachmentByte(b);
                    attachment.setAttachmentType(event.getFile().getContentType());
                    attachment.setExtensionType(FilenameUtils.getExtension(event.getFile().getFileName()));
                    selectedBugs.get(0).setAttachment(attachment);
                    bugService.save(selectedBugs.get(0));
                    FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteAttachment() {
        try {
            try {
                Attachment attachment = selectedBugs.get(0).getAttachment();
                selectedBugs.get(0).setAttachment(null);
                attachmentServiceEJB.delete(attachment);
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Missing attachment");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public StreamedContent downloadAttachment() throws IOException {
        Attachment attachment = null;
        try {
            try {
                try {
                    attachment = selectedBugs.get(0).getAttachment();

                    File file = byteToFile(attachment.getAttachmentByte(), "MyAttachment");

                    InputStream stream = new FileInputStream(file.getAbsolutePath());


                    String contentType = attachment.getAttachmentType();
                    String extension = attachment.getExtensionType();

                    return new DefaultStreamedContent(stream, contentType, "downloaded_bug_attachment." + extension);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Missing attachment");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        return null;
    }


    public StreamedContent getPDF() throws IOException {
        PDFWriter pdfWriter = null;
        try {
            try {
                try {
                    List<Bug> bugs = new ArrayList<>();
                    bugs.add(selectedBugs.get(0));

                    pdfWriter = downloadBean.getPDFWriter();

                    pdfWriter.createPDF(bugs, "Bug_Info");


                    return pdfWriter.getFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Invalid bug");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

        return null;


    }

    public StreamedContent getExcel() throws IOException {

        ExcelWriter excelWriter = null;
        try {

            excelWriter = downloadBean.getExcelWriter();
            excelWriter.createExcel(selectedBugs, "Bug_Info");

            return excelWriter.downloadAttachment();
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Invalid bug");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }


        return null;

    }

    private String data;
    private User user = null;
    private StatusType aux;

    public void updateBug(CellEditEvent event) {
        if (service.userHasRight(((User) WebHelper.getSession().getAttribute("currentUser")), RightType.BUG_MANAGEMENT)) {
            if (title != null) {
                selectedBugs.get(0).setTitle(title);
            }
            if (description != null) {
                selectedBugs.get(0).setDescription(description);
            }
            if (version != null) {
                selectedBugs.get(0).setVersion(version);
            }
            if (severityType != null) {
                selectedBugs.get(0).setSeverityType(severityType);
            }
            if (assignedTo != null) {
                try {
                    user = service.getUserByUsername(assignedTo);
                    if (user != null) {
                        selectedBugs.get(0).setAssignedTo(user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Assigned user does not exist!!");
                    RequestContext.getCurrentInstance().showMessageInDialog(message);
                }
            }

            if (statusType != null) {
                if (statusType.equals(StatusType.CLOSED)) {
                    if (service.userHasRight(((User) WebHelper.getSession().getAttribute("currentUser")), RightType.BUG_CLOSE)) {
                        selectedBugs.get(0).setStatusType(statusType);
                        selectedBugs.get(0).setFixedInVersion(selectedBugs.get(0).getVersion());
                        sendClosedNotification();
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Current user does not have bug closing right!!");
                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                    }
                } else {
                    aux = selectedBugs.get(0).getStatusType();
                    selectedBugs.get(0).setStatusType(statusType);
                    sendStatusUpdateNotification();
                }
            } else
                sendNotification();

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Current user does not have bug editing right!!");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public void sendClosedNotification() {
        bugService.update(selectedBugs.get(0));
        data = "BUG CLOSED!    Title:" + selectedBugs.get(0).getTitle() + ". Description:" + selectedBugs.get(0).getDescription() + ". Version:" + selectedBugs.get(0).getVersion() + ". Target date:" + selectedBugs.get(0).getTargetDate() + ". Severity type:" + selectedBugs.get(0).getSeverityType() + ". Assigned to:" + selectedBugs.get(0).getAssignedTo();
        Notification notification = new Notification(NotificationType.BUG_CLOSED);
        notification.setInfo(data);
        notification.setBugTitle(selectedBugs.get(0).getTitle());
        notificationServiceEJB.save(notification);
        selectedBugs.get(0).getAssignedTo().getNotifications().add(notification);
        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(notification);
    }

    public void sendStatusUpdateNotification() {
        bugService.update(selectedBugs.get(0));
        data = "BUG UPDATED!   Old status:" + aux.toString() + ". New status:" + selectedBugs.get(0).getStatusType() + ".    Title:" + selectedBugs.get(0).getTitle() + ". Description:" + selectedBugs.get(0).getDescription() + ". Version:" + selectedBugs.get(0).getVersion() + ". Target date:" + selectedBugs.get(0).getTargetDate() + ". Severity type:" + selectedBugs.get(0).getSeverityType() + ". Assigned to:" + selectedBugs.get(0).getAssignedTo();
        Notification notification = new Notification(NotificationType.BUG_STATUS_UPDATED);
        notification.setInfo(data);
        notification.setBugTitle(selectedBugs.get(0).getTitle());
        notificationServiceEJB.save(notification);
        selectedBugs.get(0).getAssignedTo().getNotifications().add(notification);
        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(notification);
    }

    public void sendNotification() {
        bugService.update(selectedBugs.get(0));
        data = "BUG UPDATED!    Title:" + selectedBugs.get(0).getTitle() + ". Description:" + selectedBugs.get(0).getDescription() + ". Version:" + selectedBugs.get(0).getVersion() + ". Target date:" + selectedBugs.get(0).getTargetDate() + ". Severity type:" + selectedBugs.get(0).getSeverityType() + ". Assigned to:" + selectedBugs.get(0).getAssignedTo();
        Notification notification = new Notification(NotificationType.BUG_UPDATED);
        notification.setInfo(data);
        notification.setBugTitle(selectedBugs.get(0).getTitle());
        notificationServiceEJB.save(notification);
        selectedBugs.get(0).getAssignedTo().getNotifications().add(notification);
        ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(notification);
    }


    public void navigate(String s) {
        Bug bug = bugService.findBugByTitle(s);
        try {
            if (!bugList.isEmpty()) {
                bugList.clear();
            }
            bugList.add(bug);
            if (!bugList.isEmpty()) {
                FacesContext context = FacesContext.getCurrentInstance();
                NavigationHandler navigationHandler = context.getApplication()
                        .getNavigationHandler();
                navigationHandler.handleNavigation(context, null, "bugPage"
                        + "?faces-redirect=true");
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "List is empty!!");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}