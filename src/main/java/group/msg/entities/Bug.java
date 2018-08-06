package group.msg.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table
public class Bug {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;
    private String description;
    private String version;
    @Column(name = "FIXED_IN_VERSION")
    private String fixedInVersion;
    @Column(name = "TARGET_DATE")
    private Date targetDate;
    @Column(name = "SEVERITY")
    private SeverityType severityType;


    @OneToOne
    @JoinColumn(name="created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name="assigned_user")
    private User assignedTo;

    private StatusType statusType;

    @ManyToOne
    @JoinColumn(name="attachement")
    private Attachment attachment;

    @ManyToOne
    @JoinColumn(name="notification")
    private Notification notification;
}