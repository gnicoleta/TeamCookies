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
                query = "select e from Bug e order by e.id desc")

})
public class Bug implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    //@descriptionValidation
    private String description;

    //@revisionValidation
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


}
