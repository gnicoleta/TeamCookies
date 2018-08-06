package group.msg.userManagement;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
public class Rights {

    enum RightType {

        PERMISSION_MANAGEMENT, USER_MANAGEMENT, BUG_MANAGEMENT,
        BUG_CLOSE, BUG_EXPORT_PDF
    }

    @Id
    @GeneratedValue
    private int ID;

    private RightType rightType;

    @ManyToMany
    @JoinTable(name = "role_right",
            joinColumns = @JoinColumn(name = "right_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public Rights() {
    }

    public Rights(RightType rightType, List<Role> roles) {
        this.rightType = rightType;
        this.roles = roles;
    }
}
