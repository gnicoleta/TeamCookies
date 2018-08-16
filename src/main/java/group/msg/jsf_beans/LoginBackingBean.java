package group.msg.jsf_beans;


import group.msg.beans.PasswordEncryptor;
import group.msg.beans.RightsForRoleGetterAndSetter;
import group.msg.entities.*;
import lombok.Data;
import org.jboss.weld.context.ejb.Ejb;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

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

    @EJB
    private RoleServiceEJB roleServiceEJB;

    @EJB
    private NotificationServiceEJB notificationServiceEJB;

    @Ejb
    private RightServiceEJB rightServiceEJB;

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
        boolean userPresentInDB = true;
        User user1 = null;

        try {
            user1 = service.getUserByUsername(username);
        } catch (Exception e) {
            userPresentInDB = false;
            e.printStackTrace();
        }


        if (userPresentInDB) {


            if (user1.getUserStatus().equals(UserStatus.ACTIVE)) {

                String encryptedInputPassword = "admin";
                if (!pwd.equals("admin")) {

                    encryptedInputPassword = passwordEncryptor.passwordEncryption(pwd);
                }

                if (encryptedInputPassword.equals(user1.getPassword())) {
                    WebHelper.getSession().setAttribute("loggedIn", true);
                    WebHelper.getSession().setAttribute("currentUser", user1);
                    if (user1.getLoginAttemptsCount() > 0) {
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


                        int nr = 0;
                        for (User user : service.getUsersWithCertainRight(RightType.USER_MANAGEMENT)) {
                            user.getNotifications().add(notification);
                            service.update(user);
                            nr++;
                        }
                        notificationServiceEJB.save(notification);
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Attempt number 5" + "\n" + "acount deactivated " + nr);
                        RequestContext.getCurrentInstance().showMessageInDialog(message);


                        return "";

                    } else {

                        user1.setLoginAttemptsCount(user1.getLoginAttemptsCount() + 1);
                        service.update(user1);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "wrong password\n" +
                                (5 - user1.getLoginAttemptsCount()) + " attempts remaining"));
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
    //}


}