package group.msg.jsf_beans;

import group.msg.beans.UsernameGenerator;
import group.msg.entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UserServiceEJB {


    @Inject
    UsernameGenerator usernameGenerator;

    @EJB
    RoleServiceEJB roleServiceEJB;

    @Inject
    private User user;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Logger logger;

    public String generateUsername(String firstname, String lastName) {
        return usernameGenerator.generateUsername(firstname, lastName, em);
    }

    public User find(Integer id) {
        return em.find(User.class, id);
    }

    public void save(User user) {
        try {
            em.persist(user);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }

    }


    public void clear() {
        em.clear();
    }

    public void update(User user) {
        try {
            em.merge(user);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void updateBug(Bug bug) {
        try {
            em.merge(bug);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public void delete(User user) {
        try {
            em.remove(em.contains(user) ? user : em.merge(user));
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    public User getUserByUsername(String username) {
        try {
            try {
                Query q = em.createNamedQuery("User.findByUsername", User.class);
                q.setParameter(1, username);
                User result = (User) q.getSingleResult();
                return result;
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (NoResultException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    public boolean findUserByUsername(String username) {
        List<User> result = null;
        try {
            Query q = em.createNamedQuery("User.findByUsername", User.class);
            q.setParameter(1, username);
            result = q.getResultList();
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        if (result.isEmpty()) {
            return false;
        } else if (result.get(0).getUsername().equals(username)) {
            return true;
        } else {
            return false;
        }

    }

    public Collection<Role> deleteRoleFromUser(Role role, String username) {
        try {
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
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    public List<User> getAllUsers() {
        try {
            try {
                Query q = em.createNamedQuery("User.findAll", User.class);
                List<User> result = q.getResultList();
                return result;
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (NoResultException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    @SuppressWarnings("Duplicates")
    public List<Bug> getAllBugs() {
        try {
            try {
                Query q = em.createNamedQuery("Bug.findAll", Bug.class);
                List<Bug> result = q.getResultList();
                return result;
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (NoResultException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
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
        try {
            List<Role> roles = (List<Role>) user.getUserRoles();
            for (Role role : roles) {
                List<Rights> rights = (List<Rights>) role.getRoleRights();
                for (Rights right : rights) {
                    if (right.getType().equals(rightType)) {
                        return true;
                    }
                }
            }
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        return false;

    }

    public void save(Rights right) {
        try {
            em.persist(right);
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }

    @SuppressWarnings("Duplicates")
    public Rights findRightByType(String type) {
        try {
            try {
                Query q = em.createNamedQuery("Rights.findByRightType", Rights.class);
                q.setParameter(1, type);
                Rights result = (Rights) q.getSingleResult();
                return result;
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        }catch (NoResultException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    public String getUserInfo(User user) {
        String s = "";
        try {
            s += "Username=" + user.getUsername() +
                    "\n" + "First Name=" + user.getFirstName() +
                    "\n" + "Last Name=" + user.getLastName() +
                    "\n" + "Email=" + user.getEmail() +
                    "\n" + "Phone=" + user.getMobileNumber();
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        return s;

    }

    public List<User> getUsersWithCertainRight(RightType rightType) {
        try {
            try {

                Query query1 = em.createQuery("select distinct u from User u,u.userRoles ur join Rights rr where rr.typeString='USER_MANAGEMENT'");

                return query1.getResultList();
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        } catch (NoResultException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    public void addRole(RoleType roleType, User user) {
        try {
            Role role = new Role();
            role.setRole(roleType);
            if (!user.getUserRoles().contains(role)) {
                Role role1 = roleServiceEJB.findRoleByType(roleType.toString());
                user.getUserRoles().add(role1);
            }
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
    }


}