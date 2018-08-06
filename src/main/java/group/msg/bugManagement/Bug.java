package group.msg.bugManagement;

import com.itextpdf.text.Document;

import java.util.Date;

enum Status {
    New, InProggres, Fixed,
    Closed, Rejected, InfoNeeded
}

enum Severity {
    Critical, High, Medium, Low
}

public class Bug {

    private String title;
    private String description;
    private String curentVersion;
    private String fixedVersion;
    private Date targetDate;

    private Severity severity;

    private String createdBy;
    private Status status = Status.New;
    private String assignedTo;

    private Document attachment;


}
