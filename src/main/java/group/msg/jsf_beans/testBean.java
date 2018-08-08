package group.msg.jsf_beans;


import group.msg.entities.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Stateless
@LocalBean
public class testBean {

    public testBean() {
// TODO Auto-generated constructor stub
    }

    //get all the rows from the category table
    public List<User> getCategory() {
        EntityManagerFactory emf;
        EntityManager em;

        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();

        String queryStr = "select cat from User cat";

        Query query;
        query = em.createQuery(queryStr);
        List<User> cat = query.getResultList();

        return cat;

    }

    public void insertData() {
        EntityManagerFactory emf;
        EntityManager em;

//the Entity Class-Category
        User cat = new User();

//set value
        cat.setId(5);
        cat.setFirstName("test cat");

//the "test" is the persist unit in persistence.xml
        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(cat);
        tx.commit();
        em.close();
        emf.close();

    }
}