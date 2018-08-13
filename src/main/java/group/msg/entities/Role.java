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
        roleString = role.toString();

    }

    @Override
    public String toString() {
        return " " + role + "\n";
    }

    @Override
    public boolean equals(Object otherObject) {
        // a quick test to see if the objects are identical
        if (this == otherObject) return true;
        // must return false if the explicit parameter is null
        if (otherObject == null) return false;
        // if the classes don't match, they can't be equal
        if (getClass() != otherObject.getClass())
            return false;
        // now we know otherObject is a non-null Employee
        Role other = (Role) otherObject;
        // test whether the fields have identical values
        return role.equals(other.role);
    }

}
