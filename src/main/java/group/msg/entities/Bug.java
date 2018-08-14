package group.msg.entities;

import lombok.Data;

import javax.persistence.*;
import javax.swing.text.Document;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import group.msg.validator.bugInfoValidator.descriptionValidation;
import group.msg.validator.bugInfoValidator.revisionValidation;

@Data
@Entity
@Table
@NamedQueries({
        @NamedQuery(name = "Bug.findAll",
                query = "select e from Bug e order by e.id asc")

})
public class Bug implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    //@descriptionValidation
    private String description;

    @revisionValidation
    private String version;

    @Column(name = "FIXED_IN_VERSION")
    private String fixedInVersion;

    @Column(name = "TARGET_DATE")
    private Date targetDate;

    @Column(name = "SEVERITY")
    private SeverityType severityType;


    @OneToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_user")
    private User assignedTo;

    private StatusType statusType = StatusType.NEW;

    @ManyToOne
    @JoinColumn(name = "attachement")
    private Attachment attachment;

    @ManyToOne
    @JoinColumn(name = "notification")
    private Notification notification;


    @Override
    public String toString(){
        StringBuilder result=new StringBuilder();
        result.append("ID: ").append(id).append(System.lineSeparator());
        result.append("Title: ").append(title).append(System.lineSeparator());
        result.append("Description: ").append(description).append(System.lineSeparator());
        result.append("Version: ").append(version).append(System.lineSeparator());
        result.append("Fixed in version: ").append(fixedInVersion).append(System.lineSeparator());
        result.append("Target date: ").append(targetDate.toString()).append(System.lineSeparator());
        result.append("Severity type: ").append(severityType.toString()).append(System.lineSeparator());
        result.append("Created by: ").append(createdBy.getUsername()).append(System.lineSeparator());
        result.append("Assigned to: ").append(assignedTo.getUsername()).append(System.lineSeparator());
        result.append("Status type: ").append(statusType.toString()).append(System.lineSeparator());


        return result.toString();


    }


}