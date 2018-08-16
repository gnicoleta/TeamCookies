package group.msg.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table
@NamedQueries({

        @NamedQuery(name = "Notification.findByNotificationType",
                query = "select sp from Notification sp where sp.notificationTypeString like ?1")

})
public class Notification implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "notification")
    private NotificationType notificationType;

    private String notificationTypeString;

    private String info;

    @Transient
    private int bugId;

    @Column(name="notification_date")
    private Date notificationDate = new Date(System.currentTimeMillis());

    @ManyToMany
    @JoinTable(name = "user_notification",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users;

    @OneToMany(mappedBy = "notification")
    private Collection<Bug> bugs;

    public Notification() {

    }

    public Notification(NotificationType notificationType) {
        this.notificationType = notificationType;
        notificationTypeString = notificationType.toString();
    }
}
