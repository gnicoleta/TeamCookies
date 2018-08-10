package group.msg.jsf_beans;
//import group.msg.test.jpa.JPABaseTest;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.RightsForRoleGetterAndSetter;
import group.msg.entities.*;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Inject
    PasswordEncryptor passwordEncryptor;

    @Inject
    RightsForRoleGetterAndSetter rightsForRoleGetterAndSetter;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public void submit(){
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correct", "Correct");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String validateUsernamePassword() {

        User userAdmin;
        boolean userPresentInDB = true;
        User user1 = null;

    // delete after testing--------
        if (username.equals("admin") && pwd.equals("admin")) {

            userAdmin=new User();
            userAdmin.setUsername(username);
            userAdmin.setPassword(pwd);


            LinkedList<String> selectedRolesStrings=new LinkedList<>();
            selectedRolesStrings.add("ADM");
            selectedRolesStrings.add("PM");
            selectedRolesStrings.add("TM");
            selectedRolesStrings.add("DEV");
            selectedRolesStrings.add("TEST");
            LinkedList<Role> selectedRoles = new LinkedList<>();

            for (String roleString : selectedRolesStrings) {
                Role role = new Role(RoleType.valueOf(roleString));

                List<RightType> rightTypes = new ArrayList<>();
                rightTypes=rightsForRoleGetterAndSetter.getRights(RoleType.valueOf(roleString));
                Right right;
                List<Right> rightList=new LinkedList<>();
                for(RightType rightType:rightTypes){

                  right =new  Right(rightType);
                    rightList.add(right);

                    service.save(right);
                }

                role.setRoleRights(rightList);


                selectedRoles.add(role);
                service.save(role);

            }
            userAdmin.setUserRoles(selectedRoles);
            service.save(userAdmin);
            WebHelper.getSession().setAttribute("currentUser",userAdmin);

            return "homepage";
            // delete after testing--------
        } else {


            try {


                user1 = service.getUserByUsername(username);
            } catch (Exception e) {
                userPresentInDB = false;
            }


            String encryptedInputPassword = passwordEncryptor.passwordEncryption(pwd);


            if (userPresentInDB && encryptedInputPassword.equals(user1.getPassword())) {
                WebHelper.getSession().setAttribute("loggedIn", true);
                WebHelper.getSession().setAttribute("currentUser",user1);
                return "homepage";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "Invalid credentials."));
                return "";
            }
        }
    }

    public String getCurrentlyLoggedInUsername() {
        return user.getUsername();
    }
}
