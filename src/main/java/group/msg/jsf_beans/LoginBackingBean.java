package group.msg.jsf_beans;


import group.msg.beans.PasswordEncryptor;
import group.msg.beans.RightsForRoleGetterAndSetter;
import group.msg.entities.*;
import lombok.Data;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@Data
@Named
@SessionScoped
public class LoginBackingBean implements Serializable {
    private String username;
    private String pwd;

    private User user;
    private List<User> users;

    @EJB
    private UserServiceEJB service;

    @Inject
    PasswordEncryptor passwordEncryptor;

    @Inject
    RightsForRoleGetterAndSetter rightsForRoleGetterAndSetter;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public void submit() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", "Correct");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String validateUsernamePassword() {


        User userAdmin;
        boolean userPresentInDB = true;
        User user1 = null;

        // delete after testing--------
        if (username.equals("admin") && pwd.equals("admin")) {

            userAdmin = new User();
            userAdmin.setUsername(username);
            userAdmin.setPassword(pwd);


            LinkedList<String> selectedRolesStrings = new LinkedList<>();
            selectedRolesStrings.add("ADM");
            selectedRolesStrings.add("PM");
            selectedRolesStrings.add("TM");
            selectedRolesStrings.add("DEV");
            selectedRolesStrings.add("TEST");
            LinkedList<Role> selectedRoles = new LinkedList<>();

            for (String roleString : selectedRolesStrings) {
                Role role = new Role(RoleType.valueOf(roleString));

                List<RightType> rightTypes = new ArrayList<>();
                rightTypes = rightsForRoleGetterAndSetter.getRights(RoleType.valueOf(roleString));
                Right right;
                List<Right> rightList = new LinkedList<>();
                for (RightType rightType : rightTypes) {

                    right = new Right(rightType);

                    rightList.add(right);

                    service.save(right);
                }

                role.setRoleRights(rightList);
                Notification notification = new Notification(NotificationType.WELCOME_NEW_USER);

                notification.setInfo("Welcome admin"+'\n'+"bla"+"\n");

                List<Notification> notifications = new LinkedList<>();

                service.save(notification);
                notifications.add(notification);

                userAdmin.setNotifications(notifications);

                selectedRoles.add(role);
                service.save(role);

            }
            userAdmin.setUserRoles(selectedRoles);
            service.save(userAdmin);
            WebHelper.getSession().setAttribute("currentUser", userAdmin);

            return "homepage";
            // delete after testing--------
        } else {


            try {


                user1 = service.getUserByUsername(username);
            } catch (Exception e) {
                userPresentInDB = false;
            }


            if (userPresentInDB) {


                if (user1.getUserStatus().equals(UserStatus.ACTIVE)) {


                    String encryptedInputPassword = passwordEncryptor.passwordEncryption(pwd);


                    if (encryptedInputPassword.equals(user1.getPassword())) {
                        WebHelper.getSession().setAttribute("loggedIn", true);
                        WebHelper.getSession().setAttribute("currentUser", user1);
                        if(user1.getLoginAttemptsCount()>0){
                            user1.setLoginAttemptsCount(0);
                            service.update(user1);
                        }
                        return "homepage";
                    } else {
                        if (user1.getLoginAttemptsCount() == 4) {

                            user1.setUserStatus(UserStatus.INACTIVE);
                            service.update(user1);

                            Notification notification = new Notification(NotificationType.USER_DEACTIVATED);
                            notification.setInfo(service.getUserInfo(user1));


                            int nr=0;
                            for(User user:service.getUsersWithCertainRight(RightType.USER_MANAGEMENT)){
                                user.getNotifications().add(notification);
                                service.update(user);
                                nr++;
                            }
                            service.save(notification);
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Attempt number 5" + "\n" + "acount deactivated "+nr);
                            RequestContext.getCurrentInstance().showMessageInDialog(message);



                            return "";

                        } else {

                            user1.setLoginAttemptsCount(user1.getLoginAttemptsCount()+1);
                            service.update(user1);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "wrong password\n"+
                                    (5- user1.getLoginAttemptsCount())+" attempts remaining"));
                            return "";
                        }
                    }
                }
                {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "account is deactivated ");
                    RequestContext.getCurrentInstance().showMessageInDialog(message);
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "wrong username"));
                return "";
            }
        }
    }


    public String getCurrentlyLoggedInUsername() {
        return user.getUsername();
    }
}