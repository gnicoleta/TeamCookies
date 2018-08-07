package group.msg.beans;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class UsernameGenerator {
    public String generateUsername(String firstName, String lastName) {

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

        EntityManager em = new EntityManagerImpl("This session");
        TypedQuery<Integer> q = em.createNamedQuery("JPAExample.findIdByName", Integer.class);
        q.setParameter(1, result);

        while (!q.getResultList().isEmpty()) {
            result.append(firstName.charAt((int) (Math.random() * firstName.length())));

            q = em.createNamedQuery("JPAExample.findIdByName", Integer.class);
            q.setParameter(1, result);

        }

        return result.toString();

    }

}
