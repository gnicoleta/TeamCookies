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

import java.util.LinkedList;
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
        }


        if (userPresentInDB) {


            if (user1.getUserStatus().equals(UserStatus.ACTIVE)) {

                String encryptedInputPassword="admin";

                if(user1.getUsername().equals("admin")){
                    Rights right1=new Rights(RightType.PERMISSION_MANAGEMENT);
                    Rights right2=new Rights(RightType.USER_MANAGEMENT);
                    Rights right3=new Rights(RightType.BUG_MANAGEMENT);
                    Rights right4=new Rights(RightType.BUG_CLOSE);
                    Rights right5=new Rights(RightType.BUG_EXPORT_PDF);
                    service.save(right1);
                    service.save(right2);
                    service.save(right3);
                    service.save(right4);
                    service.save(right5);


                    Role role1=new Role(RoleType.ADM);
                    Role role2=new Role(RoleType.PM);
                    Role role3=new Role(RoleType.TM);
                    Role role4=new Role(RoleType.DEV);
                    Role role5=new Role(RoleType.TEST);

                    LinkedList<Rights> rights1=new LinkedList<>();
                    rights1.add(right1);
                    rights1.add(right2);
                    role1.setRoleRights(rights1);
                    roleServiceEJB.save(role1);

                    LinkedList<Rights> rights2=new LinkedList<>();
                    rights2.add(right2);
                    rights2.add(right3);
                    rights2.add(right4);
                    role2.setRoleRights(rights2);
                    roleServiceEJB.save(role2);
                    LinkedList<Rights> rights3=new LinkedList<>();
                    rights3.add(right5);
                    rights3.add(right3);
                    rights3.add(right4);
                    role3.setRoleRights(rights3);
                    roleServiceEJB.save(role3);
                    LinkedList<Rights> rights4=new LinkedList<>();
                    rights4.add(right3);
                    rights4.add(right4);
                    rights4.add(right5);
                    role4.setRoleRights(rights4);
                    roleServiceEJB.save(role4);
                    LinkedList<Rights> rights5=new LinkedList<>();
                    rights5.add(right3);
                    rights5.add(right4);
                    rights5.add(right5);
                    role5.setRoleRights(rights5);
                    roleServiceEJB.save(role5);

                    LinkedList<Role>roles=new LinkedList<>();
                    roles.add(role1);
                    roles.add(role2);
                    roles.add(role3);
                    roles.add(role4);
                    roles.add(role5);
                    user1.setUserRoles(roles);
                    service.update(user1);

                }



                if(!pwd.equals("admin")) {

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