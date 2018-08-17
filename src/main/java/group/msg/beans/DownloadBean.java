package group.msg.beans;

import lombok.Data;

import javax.enterprise.context.SessionScoped;
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