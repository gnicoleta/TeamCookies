package group.msg.jsf_beans;

import group.msg.entities.Notification;

import group.msg.entities.NotificationType;
import group.msg.entities.User;
import lombok.Data;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Named
@ManagedBean
@ViewScoped
public class NotificationsBean extends LazyDataModel<Notification> implements Serializable {
    private Notification selectedNotification;
    private String outputMessage;
    private int id;
    private int bugId;

    private List<Notification> notificationList = new ArrayList<>();
    @EJB
    private NotificationServiceEJB notificationServiceEJB;

    public Notification getSelectedNotification() {
        return selectedNotification;
    }

    public void setSelectedNotification(Notification selectedNotification) {
        this.selectedNotification = selectedNotification;
        notificationServiceEJB.update(selectedNotification);
    }

    @PostConstruct
    public void init() {
        notificationList = (List<Notification>) ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications();
    }


    @Override
    public Notification getRowData(String rowKey) {
        Integer id = Integer.parseInt(rowKey);
        return notificationList.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
    }

    @Override
    public Object getRowKey(Notification object) {
        return object.getId();
    }

    @Override
    public List<Notification> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Notification> filteredList = new ArrayList<>();
        notificationList.forEach(notification -> {
            boolean match = true;
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext(); ) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);

                        Field fieldToFilter = null;
                        for (Field field : Notification.class.getDeclaredFields()) {
                            if (field.getName().equals(filterProperty)) {
                                fieldToFilter = field;
                            }
                        }

                        if (fieldToFilter != null) {
                            fieldToFilter.setAccessible(true);
                        } else {
                            continue;
                        }

                        String fieldValue = String.valueOf(fieldToFilter.get(notification));

                        if (filterValue.equals("") || filterValue == null || fieldValue.startsWith(filterValue.toString())) {
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
                filteredList.add(notification);
            }
        });

        int dataSize = filteredList.size();
        if (sortField != null) {
            filteredList.sort(new NotificationSorter(sortField, sortOrder));
        }
        this.setRowCount(dataSize);

        //paginate
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


    public static class NotificationSorter implements Comparator<Notification> {
        private String sortField;
        private SortOrder sortOrder;

        public NotificationSorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(Notification notification, Notification t1) {
            try {
                Field fieldToSort = null;
                for (Field field : Notification.class.getDeclaredFields()) {
                    if (field.getName().equals(sortField)) {
                        fieldToSort = field;
                    }
                }

                if (fieldToSort != null) {
                    fieldToSort.setAccessible(true);
                } else {
                    return 1;
                }
                Object val1 = fieldToSort.get(notification);
                Object val2 = fieldToSort.get(t1);

                int comparisonResult = ((Comparable) val1).compareTo(val2);

                return SortOrder.ASCENDING.equals(sortOrder) ? comparisonResult : (-1) * comparisonResult;
            } catch (Exception e) {
                return 1;
            }
        }

    }

    public void onRowDblClickSelect(final SelectEvent event) {
        Notification obj = (Notification) event.getObject();
        if (obj.getNotificationType().equals(NotificationType.BUG_STATUS_UPDATED) || obj.getNotificationType().equals(NotificationType.BUG_UPDATED) || obj.getNotificationType().equals(NotificationType.BUG_CLOSED)) {
            int aux = obj.getBugId();
            WebHelper.getSession().setAttribute("bugId", aux);
            FacesContext context = FacesContext.getCurrentInstance();
            NavigationHandler navigationHandler = context.getApplication()
                    .getNavigationHandler();
            navigationHandler.handleNavigation(context, null, "singleBugPage"
                    + "?faces-redirect=true");
        }
    }
}