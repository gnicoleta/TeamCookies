package group.msg.jsf_beans;


import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@SessionScoped
public class UserManagement implements Serializable {


    private String outcome;

    public void navigate() {
        FacesContext context = FacesContext.getCurrentInstance();
        NavigationHandler navigationHandler = context.getApplication()
                .getNavigationHandler();
        navigationHandler.handleNavigation(context, null, outcome
                + "?faces-redirect=true");
    }
}
