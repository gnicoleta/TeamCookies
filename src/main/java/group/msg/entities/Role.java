package group.msg.entities;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table
@NamedQueries({

        @NamedQuery(name = "Role.findByRoleType",
                query = "select sp from Role sp where sp.roleString like ?1")


})
public class Role implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private RoleType role;

    @Column(name = "role_string")
    private String roleString;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> users;


    @ManyToMany
    @JoinTable(name = "role_right",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "right_id"))
    private Collection<Rights> roleRights;

    public Role() {

    }

    public Role(RoleType role) {
        this.role = role;
        roleString=role.toString();

    }

    @Override
    public String toString() {
        return " " + role + "\n";
    }

}
