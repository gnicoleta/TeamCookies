package group.msg.beans;

import javax.inject.Inject;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Logger;


public class UsernameGenerator implements Serializable {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Logger logger;

    public String generateUsername(String firstName, String lastName, EntityManager entityManager) {
        try {
            em = entityManager;
        }catch (NullPointerException e){
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        StringBuilder result = new StringBuilder();

        try {
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
        }catch (NullPointerException e){
            logger.info(Arrays.toString(e.getStackTrace()));
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

            logger.info(Arrays.toString(e.getStackTrace()));
        }


        return result.toString();

    }

}
