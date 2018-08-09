package group.msg.jsf_beans;

import java.awt.*;
import java.util.List;

import group.msg.entities.*;
import group.msg.jsf_beans.UserServiceEJB;
import lombok.Data;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
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

    private String onCLickMsg = "nume";

    private Bug selectedBug;

    List<Bug> bugList;

    /*public BugBean(){
        bugList = new ArrayList<>();
    }

    public void initialiseList() {
        bugList =  service.getAllBugs();
    }*/

    //delete these comments after u read them :))
//asa iti iei lista de buguri ca sa ti-o afisezi in datatable fara sa fie nevoie de metadata in jsf
//    <f:metadata>
//            <f:event type="preRenderView" listener="#{bugBean.initialiseList}"/>
//        </f:metadata>
//si fara sa fie nevoie sa scazi din id-1
//nu cred ca mai e nevoie de constructoru de mai sus si de metoda initialiseList()
    @PostConstruct
    public void init() {
        bugList = bugService.getAllBugs();
    }

    //cam ai nevoie sa suprascrii metodele astea daca vrei sa poti edita in celula
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
//    public void onRowClick(Event e) {
//        this.selectedBug = e.data;
//    }

    public Bug getSelectedBug() {
        return selectedBug;
    }

    public void setSelectedBug(Bug selectedBug) {
        this.selectedBug = selectedBug;
    }

    public void rowSelected(SelectEvent event) {
        this.updateBugTitle(selectedBug.getTitle());
        this.updateBugDescription(selectedBug.getDescription());
    }

    public void updateBugTitle(String newTitle) {
        selectedBug.setTitle(newTitle);
        bugService.update(selectedBug);
    }

    public void updateBugDescription(String newDescription) {
        selectedBug.setDescription(newDescription);
        bugService.update(selectedBug);
    }

    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() {
        if(file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
