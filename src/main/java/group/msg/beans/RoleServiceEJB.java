package group.msg.beans;


import group.msg.entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    public Role findRoleByType(String type) {
        Query q = em.createNamedQuery("Role.findByRoleType", Role.class);
        q.setParameter(1, type);
        Role result = (Role) q.getSingleResult();
        return result;
    }
}
