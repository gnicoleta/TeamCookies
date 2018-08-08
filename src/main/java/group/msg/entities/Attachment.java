package group.msg.entities;

import lombok.Data;

import javax.persistence.*;
import javax.swing.text.Document;
import java.util.Collection;

@Data
@Entity
@Table
public class Attachment {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "ATTACHEMENT_TYPE")
    private AttachmentType attachmentType;



    @OneToMany(mappedBy = "attachment")
    private Collection<Bug> bugs;

}
