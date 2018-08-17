package group.msg.blogic;

import javax.persistence.*;
import java.io.Serializable;


public class UsernameGenerator implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public String generateUsername(String firstName, String lastName, EntityManager entityManager) {
        em = entityManager;

        StringBuilder result = new StringBuilder();
        if (lastName.length() < 5) {
            result.append(lastName);
            int i = 0;
            while (result.toString().length() < 6) {
                if (i == firstName.length()) {
                    i = 0;
                }
                result.append(firstName.charAt(i));
                i++;
            }
        } else {
            for (int i = 0; i < 5; i++) {
                result.append(lastName.charAt(i));
            }
            result.append(firstName.charAt(0));
        }

        try {
            TypedQuery<Integer> q = em.createNamedQuery("User.findIdByName", Integer.class);
            q.setParameter(1, result.toString());

            while (!q.getResultList().isEmpty()) {
                result.append(firstName.charAt((int) (Math.random() * firstName.length())));

                q = em.createNamedQuery("User.findIdByName", Integer.class);
                q.setParameter(1, result.toString());

            }
        } catch (NullPointerException e) {

            e.printStackTrace();
            System.out.println("Hello " + em);
        }


        return result.toString();

    }

}
