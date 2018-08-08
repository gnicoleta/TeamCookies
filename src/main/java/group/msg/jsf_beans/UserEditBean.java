package group.msg.jsf_beans;


import group.msg.entities.User;
import lombok.Data;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Data
@Named
@SessionScoped
public class UserEditBean implements Serializable {


    private String outcome;
    private String userName;
    @EJB
    private UserServiceEJB service;

    @PersistenceContext
    private EntityManager em;

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


    public String ifExistsDelete(){
        return service.ifExistsDelete(userName);
    }

    public void ifExistsActivate(){
        service.ifExistsActivate(userName);
    }

}
