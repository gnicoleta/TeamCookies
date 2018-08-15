package group.msg.jsf_beans;


import group.msg.entities.Rights;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RightServiceEJB {

    @PersistenceContext
    private EntityManager em;
    public void update(Rights right) {
        em.merge(right);
    }

    public void save(Rights right) {
        em.persist(right);
    }

    public Rights findRightByType(String type){
        Query q = em.createNamedQuery("Rights.findByRightType", Rights.class);
        q.setParameter(1, type);
        Rights result = (Rights) q.getSingleResult();
        return result;
    }
}
