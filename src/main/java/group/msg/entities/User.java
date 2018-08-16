package group.msg.entities;

import group.msg.validator.emailValidation.emailValidation;
import group.msg.validator.mobileNumberValidation.mobileNumberValidation;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

@Data
@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(name = "User.findAll",
                query = "select e from User e order by e.id desc"),
        @NamedQuery(name = "User.findById",
                query = "select e from User e where e.id = :id"),
        @NamedQuery(name = "User.findByUsername",
                query = "select sp from User sp where sp.username like ?1")


})
@NamedNativeQueries({
        @NamedNativeQuery(name = "User.findIdByName",
                query = "select sp.id from user sp where sp.USERNAME like ?1")
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
    @NonNull
    private String mobileNumber;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> userRoles;

    @NotNull
    @emailValidation
    private String email;

    private UserStatus userStatus = UserStatus.ACTIVE;

    @ManyToMany
    @JoinTable(name = "user_notification",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id"))
    private Collection<Notification> notifications;


    @OneToMany(mappedBy = "assignedTo")
    private Collection<Bug> assignedBugs;

    @OneToOne(mappedBy = "createdBy")
    private Bug createdBug;

    private String password;

    @Column(name = "COUNT")
    private int loginAttemptsCount=0;

    @Transient
    private RoleType roleType;

    public User() {

    }

    public User(String firstName, String lastName, String username,
                Collection<Role> userRoles, @NotNull @Email String email,
                String password, String mobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.userRoles = userRoles;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;


    }

    public String toString(){
        return username;
    }



}
