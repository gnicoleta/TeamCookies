package group.msg.jsf_beans;

import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@SessionScoped
public class LoginBackingBean implements Serializable {
    private String user;
    private String pwd;

    public String validateUsernamePassword() {
        if (user.equals("admin") && pwd.equals("admin")) {
            WebHelper.getSession().setAttribute("loggedIn",true);
            return "homepage";
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "Invalid credentials."));
            return "";
        }
    }

    public String getCurrentlyLoggedInUsername(){
        return user;
    }
}
