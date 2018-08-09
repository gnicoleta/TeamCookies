package group.msg.jsf_beans;

import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Data
@Named
@SessionScoped
public class DownloadBean implements Serializable {

    @Inject
    private Download download;

    @Inject
    private ExcelWriter excelWriter;


}
