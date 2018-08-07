package group.msg.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table
public class Notification implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "notification")
    private NotificationType notificationType;

    @ManyToMany
    @JoinTable(name = "user_notification",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users;

    @OneToMany(mappedBy = "notification")
    private Collection<Bug> bugs;
    public Notification(){

    }
    public Notification(NotificationType notificationType){
        this.notificationType=notificationType;
    }
}
