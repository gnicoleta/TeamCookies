package group.msg.userManagement;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table
public class Role {

    enum RoleType {

        Administrator, ProjectManager, TestManager,
        Developer, Tester

    }

    @Id
    @GeneratedValue
    private int ID;

    private RoleType roleType;

    @ManyToMany
    @JoinTable(name = "role_right",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "right_id"))
    private List<Rights>rights;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;


    public Role() {
    }

    public Role(RoleType roleType, List<Rights> rights) {
        this.roleType = roleType;
        this.rights = rights;
    }
}
