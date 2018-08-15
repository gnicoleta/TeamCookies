package group.msg.jsf_beans;


import group.msg.entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

@Stateless
public class RoleServiceEJB {
    @PersistenceContext
    private EntityManager em;

    @EJB
    private RightServiceEJB rightServiceEJB;

    public void save(Role role) {
        em.persist(role);
    }
    public void update(Role role) {
        em.merge(role);
    }

    public Role findRoleByType(String type){
        Query q = em.createNamedQuery("Role.findByRoleType", Role.class);
        q.setParameter(1, type);
        Role result = (Role) q.getSingleResult();
        return result;
    }


    public Role deleteRightFromRole(String type, String rightType) {
        Query q = em.createNamedQuery("Role.findByRoleType", Role.class);
        q.setParameter(1, type);
        Role role = (Role) q.getSingleResult();

        if (role != null) {
            Collection<Rights> rights = role.getRoleRights();

            Rights rgt = new Rights();
            rgt = rightServiceEJB.findRightByType(rightType);
            if (!rights.contains(rgt)) {
                rights.remove(rgt);
                role.setRoleRights(rights);
                update(role);
            }

            return role;
        } else {
            return null;
        }
    }

    public Role addRightToRole(String type, String rightType) {

        Query q = em.createNamedQuery("Role.findByRoleType", Role.class);
        q.setParameter(1, type);
        Role role = (Role) q.getSingleResult();

        Collection<Rights> rights = role.getRoleRights();
        rights.add(rightServiceEJB.findRightByType(rightType));
        role.setRoleRights(rights);
        update(role);
        return role;
    }

}
