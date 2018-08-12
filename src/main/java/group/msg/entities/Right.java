package group.msg.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "rights")
public class Right implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;


    private RightType type;

    private String typeString;

    @ManyToMany
    @JoinTable(name = "role_right",
            joinColumns = @JoinColumn(name = "right_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public Right(RightType type) {
        this.type = type;
        this.typeString=type.toString();
    }
    public Right(){}
}
