package group.msg.jsf_beans;

import group.msg.entities.Role;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RoleServiceEJB {
    @PersistenceContext
    private EntityManager em;

    public void save(Role role) {
        em.persist(role);
    }

    public Role findRoleByType(String type){
        Query q = em.createNamedQuery("Role.findByRoleType", Role.class);
        q.setParameter(1, type);
        Role result = (Role) q.getSingleResult();
        return result;
    }

}
