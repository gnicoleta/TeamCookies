package group.msg.jsf_beans;

import group.msg.entities.User;
import lombok.Data;
import org.primefaces.model.LazyDataModel;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@ViewScoped
public class NotificationsBean  extends LazyDataModel<User> implements Serializable {


}
