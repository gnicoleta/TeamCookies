package group.msg.beans;

import group.msg.entities.Role;
import group.msg.entities.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class RegisterUserBean {

    @Inject
    PasswordEncryptor passwordEncryptor;

    @Inject
    UsernameGenerator usernameGenerator;

    private String firstName;
    private String lastName;

    private String email;
    private String password;

    private LinkedList<Role> selectedRoles;

    @PersistenceContext
    private EntityManager entityManager;

    public void registerUser() {

        User user = new User(firstName, lastName, usernameGenerator.generateUsername(firstName, lastName),
                selectedRoles, email, passwordEncryptor.passwordEncryption(password));
        entityManager.persist(user);

    }


}
