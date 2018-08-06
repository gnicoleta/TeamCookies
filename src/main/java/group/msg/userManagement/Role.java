package group.msg.userManagement;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;


@Data
@Entity
@Table
public class Role {

    @Id
    @GeneratedValue
    private int ID;

    private RoleType roleType;

    @ManyToMany
    @JoinTable(name = "role_right",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "right_id"))
    private Collection<Rights> rights;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users;


    public Role() {
    }

    public Role(RoleType roleType, Collection<Rights> rights) {
        this.roleType = roleType;
        this.rights = rights;
    }
}
