package group.msg.beans;

import group.msg.entities.Role;
import group.msg.entities.RoleType;
import group.msg.entities.User;
import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;

@Data
@Named
@SessionScoped
public class RegisterUserBean implements Serializable {

    @Inject
    PasswordEncryptor passwordEncryptor;

    @Inject
    UsernameGenerator usernameGenerator;

    private String firstName;
    private String lastName;

    private String email;
    private String password;

    private String mobileNumber;

    private LinkedList<String> selectedRolesStrings;

    private LinkedList<Role> selectedRoles;

    @PersistenceContext
    private EntityManager entityManager;

    public void registerUser() {


        for(String roleString:selectedRolesStrings){
            Role role=new Role(RoleType.valueOf(roleString));
            selectedRoles.add(role);
            entityManager.persist(role);

        }


        User user = new User(firstName, lastName, usernameGenerator.generateUsername(firstName, lastName,entityManager),
                selectedRoles, email, passwordEncryptor.passwordEncryption(password));
        entityManager.persist(user);

    }


}
