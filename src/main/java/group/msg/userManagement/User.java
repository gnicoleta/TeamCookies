package group.msg.userManagement;


import lombok.Data;

import java.util.Collection;

import group.msg.userManagement.validator.emailValidation.emailValidation;
import group.msg.userManagement.validator.mobileNumberValidation.mobileNumberValidation;
import lombok.NonNull;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;

import javax.persistence.*;

@Data
@Table
@Entity
@NamedQueries({
        @NamedQuery(name = "JPAExample.findUsers",
                query = "select e from User e order by e.ID desc"),
        @NamedQuery(name = "JPAExample.findID",
                query = "select e from User e where e.ID = :ID")
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "JPAExample.findUserName",
                query = "select sp.ID from USER sp where sp.USERNAME like ?1")
})
public class User {

    @Id
    @GeneratedValue
    private int ID;

    @NonNull
    private String familyName;

    @NonNull
    private String givenName;

    @mobileNumberValidation
    @NonNull
    private String mobileNumber;

    @emailValidation
    @NonNull
    private String email;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @NonNull
    private Collection<Role> roles;

    private String userName;

    public User() {
    }

    public User(String familyName, String givenName, String mobileNumber, String email) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.mobileNumber = mobileNumber;
        this.email = email;

        this.userName=generateUserName(this.familyName, this.givenName);
    }

    private String generateUserName(String familyName, String givenName){

        StringBuilder result=new StringBuilder();
        if(familyName.length()<5){
            result.append(familyName);
            int i=0;
            while(result.toString().length()<6){
                if(i==givenName.length()){
                    i=0;
                }
                result.append(givenName.charAt(i));
                i++;
            }
        }
        else{
            for(int i=0;i<5;i++){
                result.append(familyName.charAt(i));
            }
            result.append(givenName.charAt(0));
        }

        EntityManager em=new EntityManagerImpl("This session");
        TypedQuery<Integer> q = em.createNamedQuery("JPAExample.findUserName",Integer.class);
        q.setParameter(1, result);

        while(!q.getResultList().isEmpty()){
            result.append(givenName.charAt((int)(Math.random()*givenName.length())));

            q = em.createNamedQuery("JPAExample.findUserName",Integer.class);
            q.setParameter(1, result);

        }

        return result.toString();

    }

}
