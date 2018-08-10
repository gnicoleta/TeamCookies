package group.msg.jsf_beans;

import lombok.Data;


import javax.annotation.ManagedBean;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;

@Data
@Named
@SessionScoped
public class DownloadBean implements Serializable {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PDFWriter PDFWriter;

    @Inject
    private ExcelWriter excelWriter;


}
