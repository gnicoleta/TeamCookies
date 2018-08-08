package group.msg.jsf_beans;


import group.msg.entities.User;
import lombok.Data;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Named
@ViewScoped
public class UserEditBean extends LazyDataModel<User> implements Serializable {


    private String outcome;
    private String userName;

    private String wtv;

    private User selectedUser;
    private String outputMessage;

    private List<User> usersList;

    @EJB
    private UserServiceEJB service;


    @PostConstruct
    public void init() {
        usersList = service.getAllUsers();
    }


    public void navigate() {
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler = context.getApplication()
                .getNavigationHandler();
        navigationHandler.handleNavigation(context, null, outcome
                + "?faces-redirect=true");
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login";
    }


    public void ifExistsDelete() {
        //return service.ifExistsDelete(userName);

        if (service.ifExistsDelete(userName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User status: inactive", "User deleted."));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User not found", "User not found."));
        }
    }

    public void ifExistsActivate() {
        //service.ifExistsActivate(userName);
        if (service.ifExistsActivate(userName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User status: active", "User activated."));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User not found", "User not found."));
        }
    }

    @Override
    public User getRowData(String rowKey) {
        Integer id = Integer.parseInt(rowKey);
        return usersList.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
    }

    @Override
    public Object getRowKey(User object) {
        return object.getId();
    }

    @Override
    public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<User> filteredList = new ArrayList<>();
        usersList.forEach(user -> {
            boolean match = true;
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext(); ) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(user.getClass().getField(filterProperty).get(user));

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
                filteredList.add(user);
            }
        });

        int dataSize = filteredList.size();
        if (sortField != null) {
            filteredList.sort(new UserSorter(sortField, sortOrder));
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

    public static class UserSorter implements Comparator<User> {
        private String sortField;
        private SortOrder sortOrder;

        public UserSorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(User user, User t1) {
            try {
                Object val1 = User.class.getField(sortField).get(user);
                Object val2 = User.class.getField(sortField).get(t1);

                int comparationResult = ((Comparable) val1).compareTo(val2);

                return SortOrder.ASCENDING.equals(sortOrder) ? comparationResult : (-1) * comparationResult;
            } catch (Exception e) {
                return 1;
            }
        }
    }

    public void rowSelected(SelectEvent event) {
        outputMessage = selectedUser.getUsername();

        //selectedUser.setUsername("bbbb");
        // service.update(selectedUser);
        this.updateUsername(selectedUser.getUsername());


        //service.update(selectedUser);
    }

    public void updateUsername(String ooo) {
        selectedUser.setUsername(ooo);
        service.update(selectedUser);
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Item Edited", ((User) event.getObject()).getUsername());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        this.updateUsername(((User) event.getObject()).getUsername());
    }
}
