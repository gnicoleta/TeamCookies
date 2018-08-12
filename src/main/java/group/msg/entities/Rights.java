package group.msg.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "rights")
@NamedQueries({

        @NamedQuery(name = "Rights.findByRightType",
                query = "select sp from Rights sp where sp.typeString like ?1")


})
public class Rights implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;


    private RightType type;

    @Column(name = "type_string")
    private String typeString;

    @ManyToMany
    @JoinTable(name = "role_right",
            joinColumns = @JoinColumn(name = "right_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public Rights(RightType type) {
        this.type = type;
        this.typeString=type.toString();
    }
    public Rights(){}
}
