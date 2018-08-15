package group.msg.jsf_beans;


import group.msg.entities.*;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Named
@ViewScoped
public class UserEditBean extends LazyDataModel<User> implements Serializable {

    @EJB
    private UserServiceEJB service;
    @EJB
    private NotificationServiceEJB notificationServiceEJB;
    @EJB
    private RoleServiceEJB roleServiceEJB;
    @EJB
    private RightServiceEJB rightServiceEJB;

    private String outcome;
    private User selectedUser;
    private String outputMessage;
    private List<User> usersList;
    private String userName;
    private UserStatus userStatus;
    private String newFirstName;
    private String newLastName;
    private String newEmail;
    private String newMobileNumber;
    private RoleType roleType;
    private RoleType roleTypeDelete;

    private RightType rightType;
    private RoleType roleTypeRights;

    private String rghtTypeStr;
    private String roleTypeStr;

    @PostConstruct
    public void init() {
        usersList = service.getAllUsers();
    }

    public void navigate() {
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler = context.getApplication()
                .getNavigationHandler();
        User user = (User) WebHelper.getSession().getAttribute("currentUser");
        boolean hasRight = false;
        String requiredRight = "";

        if (outcome.equals("register") || outcome.equals("editUser")) {
            hasRight = service.userHasRight(user, RightType.USER_MANAGEMENT);
            requiredRight = RightType.USER_MANAGEMENT.toString();
        }
        if (outcome.equals("bugManagement") || outcome.equals("AddBug")) {
            hasRight = service.userHasRight(user, RightType.BUG_MANAGEMENT);
            requiredRight = RightType.BUG_MANAGEMENT.toString();
        }

        if (hasRight) {
            navigationHandler.handleNavigation(context, null, outcome
                    + "?faces-redirect=true");
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Rights", "Required right: " + requiredRight);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login";
    }

    public void ifExistsDelete() {
        if (service.ifExistsDelete(userName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User status: inactive", "User deleted."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User not found", "Try another username or contact an administrator for help."));
        }
    }

    public void ifExistsActivate() {
        if (service.ifExistsActivate(userName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User status: active", "User activated."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User not found", "Try another username or contact an administrator for help."));
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

                        Field fieldToFilter = null;
                        for (Field field : User.class.getDeclaredFields()) {
                            if (field.getName().equals(filterProperty)) {
                                fieldToFilter = field;
                            }
                        }
                        if (fieldToFilter != null) {
                            fieldToFilter.setAccessible(true);
                        } else {
                            continue;
                        }
                        String fieldValue = String.valueOf(fieldToFilter.get(user));
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
    }

    public void updateFirstName() {
        if (newFirstName != null && newFirstName != selectedUser.getFirstName() && newFirstName != "" && newFirstName != " ") {
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            String allInfo = "First Name changed: (new)" + newFirstName + " - (old)" + selectedUser.getFirstName();
            selectedUser.setFirstName(newFirstName);
            notification.setInfo(allInfo);
            notificationServiceEJB.save(notification);
            selectedUser.getNotifications().add(notification);
            service.update(selectedUser);
        }
    }

    public void updateEmail() {
        if (newEmail != null && newEmail != selectedUser.getEmail() && newEmail != "" && newEmail != " ") {
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            String allInfo = "Email changed: (new)" + newEmail + " - (old)" + selectedUser.getEmail();
            selectedUser.setEmail(newEmail);
            notification.setInfo(allInfo);
            notificationServiceEJB.save(notification);
            selectedUser.getNotifications().add(notification);
            service.update(selectedUser);
        }
    }

    public void updateMobileNumber() {
        if (newMobileNumber != null && newMobileNumber != selectedUser.getMobileNumber() && newMobileNumber != "" && newMobileNumber != " ") {
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            String allInfo = "Mobile Number changed: (new)" + newMobileNumber + " - (old)" + selectedUser.getMobileNumber();
            selectedUser.setMobileNumber(newMobileNumber);
            notification.setInfo(allInfo);
            notificationServiceEJB.save(notification);
            selectedUser.getNotifications().add(notification);
            service.update(selectedUser);
        }
    }

    public void updateRoles() {
        if (roleType != null) {
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            String allInfo = "Added new role: (new)" + this.roleType;
            service.addRole(this.roleType, selectedUser);
            notification.setInfo(allInfo);
            notificationServiceEJB.save(notification);
            selectedUser.getNotifications().add(notification);
            service.update(selectedUser);
        }
    }

    public void updateLastName() {
        if (newLastName != null && newLastName != selectedUser.getLastName() && newLastName != "" && newLastName != " ") {
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            String allInfo = "Last Name changed: (new)" + newLastName + " - (old)" + selectedUser.getLastName();
            selectedUser.setLastName(newLastName);
            notification.setInfo(allInfo);
            notificationServiceEJB.save(notification);
            selectedUser.getNotifications().add(notification);
            service.update(selectedUser);
        }
    }

    public void update() {
        updateFirstName();
        updateLastName();
        updateEmail();
        updateMobileNumber();
        updateRoles();
        updateStatus();
    }

    public void updateStatus() {
        if (userStatus != null) {
            selectedUser.setUserStatus(userStatus);
            service.update(selectedUser);
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            String allInfo = "Staus changed: (new)" + userStatus + " - (old)" + selectedUser.getUserStatus();
            notification.setInfo(allInfo);
            notificationServiceEJB.save(notification);
            selectedUser.getNotifications().add(notification);
            ((User) WebHelper.getSession().getAttribute("currentUser")).getNotifications().add(notification);
        }
    }

    public void refresh() {
        RequestContext.getCurrentInstance().execute("setTimeout(function () { location.reload(1); }, 5000);");
    }

    public void deleteRole() {
        Role role = new Role();
        role.setRole(roleTypeDelete);
        if (service.deleteRoleFromUser(role, userName) != null) {
            Notification notification = new Notification(NotificationType.USER_UPDATED);
            String allInfo = "Role: " + roleTypeDelete + " was deleted";
            selectedUser.setUserRoles(service.deleteRoleFromUser(role, userName));
            notification.setInfo(allInfo);
            notificationServiceEJB.save(notification);
            service.update(selectedUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role: " + role.getRole() + " was deleted", "User: " + selectedUser.getUsername()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User not found", "Role not found."));
        }

    }

    public void addRight() {

        Role role = roleServiceEJB.findRoleByType(roleTypeStr);
        Rights rght = new Rights();

        if (role != null) {
            Collection<Rights> rights = role.getRoleRights();
            rght = rightServiceEJB.findRightByType(rghtTypeStr);
            rights.add(rght);
            role.setRoleRights(rights);
            roleServiceEJB.update(role);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role: " + role.getRole(), "Right: " + rght.getType() + " was added"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role: " + role.getRole() + " was not found", "Right: " + rght.getType() + " was not found"));

        }
        roleServiceEJB.update(role);

    }

    public void deleteRight() {

        Role role = roleServiceEJB.findRoleByType(roleTypeStr);
        Rights rght = new Rights();

        if (role != null) {
            Collection<Rights> rights = role.getRoleRights();
            rght = rightServiceEJB.findRightByType(rghtTypeStr);
            rights.remove(rght);
            role.setRoleRights(rights);
            roleServiceEJB.update(role);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role: " + role.getRole(), "Right: " + rght.getType() + " was removed"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Role: " + role.getRole() + " was not found", "Right: " + rght.getType() + " was not found"));

        }
        roleServiceEJB.update(role);
    }
}
