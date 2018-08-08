package group.msg.jsf_beans;

import java.util.List;
import group.msg.entities.*;
import group.msg.jsf_beans.UserServiceEJB;
import lombok.Data;
import org.jboss.weld.util.LazyValueHolder;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIColumn;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;


@Named
@SessionScoped
@Data
public class BugBean extends LazyDataModel<Bug> implements Serializable {
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

    private String onCLickMsg = "nume";

    private Bug selectedBug;

    List<Bug> bugList;

    public BugBean(){
        bugList = new ArrayList<>();
    }

    public void initialiseList() {
        bugList =  service.getAllBugs();
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
                        for(Field field : Bug.class.getDeclaredFields()) {
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
        if (dataSize>pageSize) {
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
                for(Field field : Bug.class.getDeclaredFields()) {
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
    public Bug getSelectedBug(){
        return selectedBug;
    }
    public void setSelectedBug(Bug selectedBug) {
        this.selectedBug = selectedBug;
    }

    public void rowSelected(SelectEvent event) {
        onCLickMsg=selectedBug.getTitle();
    }
    public void updateBugs(){

        service.updateBug(selectedBug);
    }
}
