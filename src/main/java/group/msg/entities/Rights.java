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
        this.typeString = type.toString();
    }

    public Rights() {
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
        Rights other = (Rights) otherObject;
        // test whether the fields have identical values
        return type.equals(other.type);
    }
}
