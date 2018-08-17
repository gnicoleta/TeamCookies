package group.msg.beans;

import group.msg.blogic.UsernameGenerator;
import group.msg.entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Collection;
import java.util.List;

@Stateless
public class UserServiceEJB {


    @Inject
    UsernameGenerator usernameGenerator;

    @EJB
    RoleServiceEJB roleServiceEJB;

    @PersistenceContext
    private EntityManager em;

    public String generateUsername(String firstname, String lastName) {
        return usernameGenerator.generateUsername(firstname, lastName, em);
    }

    public User find(Integer id) {
        return em.find(User.class, id);
    }

    public void save(User user) {
        em.persist(user);

    }


    public void clear() {
        em.clear();
    }

    public void update(User user) {
        em.merge(user);
    }

    public void updateBug(Bug bug) {
        em.merge(bug);
    }

    public void delete(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }

    public User getUserByUsername(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();
        return result;
    }

    public boolean findUserByUsername(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        List<User> result = q.getResultList();
        if (result.isEmpty()) {
            return false;
        } else if (result.get(0).getUsername().equals(username)) {
            return true;
        } else {
            return false;
        }

    }

    public Collection<Role> deleteRoleFromUser(Role role, String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        List<User> result = q.getResultList();
        if (result.isEmpty()) {
            return null;
        } else if (result.get(0).getUsername().equals(username) && result.get(0).getUserRoles().contains(role)) {
            result.get(0).getUserRoles().remove(role);
            update(result.get(0));
            return result.get(0).getUserRoles();
        } else {
            return null;
        }
    }

    public List<User> getAllUsers() {
        Query q = em.createNamedQuery("User.findAll", User.class);
        List<User> result = q.getResultList();
        return result;
    }

    public List<Bug> getAllBugs() {
        Query q = em.createNamedQuery("Bug.findAll", Bug.class);
        List<Bug> result = q.getResultList();
        return result;
    }

    public boolean ifExistsDelete(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();
        if (result != null) {
            result.setUserStatus(UserStatus.INACTIVE);
            this.update(result);
            return true;
        } else {
            return false;
        }
    }

    public boolean ifExistsActivate(String username) {
        Query q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter(1, username);
        User result = (User) q.getSingleResult();
        if (result != null) {
            result.setUserStatus(UserStatus.ACTIVE);
            this.update(result);
            return true;
        } else {
            return false;
        }
    }


    public boolean userHasRight(User user, RightType rightType) {
        List<Role> roles = (List<Role>) user.getUserRoles();
        for (Role role : roles) {
            List<Rights> rights = (List<Rights>) role.getRoleRights();
            for (Rights right : rights) {
                if (right.getType().equals(rightType)) {
                    return true;
                }
            }
        }
        return false;

    }

    public void save(Rights right) {
        em.persist(right);
    }

    public Rights findRightByType(String type) {
        Query q = em.createNamedQuery("Rights.findByRightType", Rights.class);
        q.setParameter(1, type);
        Rights result = (Rights) q.getSingleResult();
        return result;
    }

    public String getUserInfo(User user) {
        String s = "";
        s += "Username=" + user.getUsername() +
                "\n" + "First Name=" + user.getFirstName() +
                "\n" + "Last Name=" + user.getLastName() +
                "\n" + "Email=" + user.getEmail() +
                "\n" + "Phone=" + user.getMobileNumber();
        return s;

    }

    public List<User> getUsersWithCertainRight(RightType rightType) {

        Query query1 = em.createQuery("select distinct u from User u,u.userRoles ur join Rights rr where rr.typeString='USER_MANAGEMENT'");

        return query1.getResultList();

    }

    public void addRole(RoleType roleType, User user) {
        Role role = new Role();
        role.setRole(roleType);
        if (!user.getUserRoles().contains(role)) {
            Role role1 = roleServiceEJB.findRoleByType(roleType.toString());
            user.getUserRoles().add(role1);
        }
    }


}