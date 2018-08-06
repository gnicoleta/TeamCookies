package group.msg.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table
public class Notification {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "notification")
    private NotificationType notificationType;

    @ManyToMany
    @JoinTable(name = "user_notifacation",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users;

    @OneToMany(mappedBy = "notification")
    private Collection<Bug> bugs;
}
