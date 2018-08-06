package group.msg.entities;

import group.msg.validator.emailValidation.emailValidation;
import group.msg.validator.mobileNumberValidation.mobileNumberValidation;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(name = "JPAExample.findAll",
                query = "select e from User e order by e.id desc"),
        @NamedQuery(name = "JPAExample.findById",
                query = "select e from User e where e.id = :id")
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "JPAExample.findIdByName",
                query = "select sp.id from user sp where sp.user_name like ?1")
})
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;

    private String username;

    @mobileNumberValidation
    private String mobileNumber;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> userRoles;

    @NotNull
    @emailValidation
    private String email;


    @ManyToMany
    @JoinTable(name = "user_notifacation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id"))
    private Collection<Notification> notifications;


    @OneToMany(mappedBy = "assignedTo")
    private Collection<Bug> assignedBugs;

    @OneToOne(mappedBy = "createdBy")
    private Bug createdBug;
}
