package group.msg.test.jpa;

import group.msg.beans.PasswordEncryptor;
import group.msg.beans.UsernameGenerator;
import group.msg.entities.*;
import group.msg.test.MavenArtifactResolver;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

@RunWith(Arquillian.class)
public class UserCRUDTest extends JPABaseTest {

    private static final int NUMBER_OF_ENTITIES = 30;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
                .addPackages(true, "group.msg")
                .addAsLibraries(MavenArtifactResolver.resolve(
                        "mysql:mysql-connector-java:5.1.46"))
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Test
    public void testCreateSimpleEntity() {
        System.out.println("Checking number of created entities...");

        Query q = em.createNamedQuery("User.findAll");
        Assert.assertEquals("Entities not found in the database!", NUMBER_OF_ENTITIES, q.getResultList().size());
    }


    @Override
    public void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (int i = 1; i <= NUMBER_OF_ENTITIES; i++) {
            User e = new User();
            UsernameGenerator usernameGenerator = new UsernameGenerator();
            PasswordEncryptor passwordEncryptor = new PasswordEncryptor();

            String lastName = "Popescu";
            String firstName = "Ion";
            String password = "admin" + i;
            String mail = "re" + i + "@msggroup.com";
            String mobileNumber = "+4045678901" + i % 10;
            String germanNumber = "+494567890123" + i % 10;


            RightType rightType = RightType.BUG_MANAGEMENT;
            RoleType roleType = RoleType.ADM;
            Bug bug = new Bug();
            bug.setDescription("Description " + i);
            bug.setTitle("Title " + i);
            bug.setVersion("a.1." + i % 10);

           // Date localDateTime=LocalDateTime.now().plusDays(i);
            Date localDateTime=new Date();
            bug.setTargetDate(localDateTime);


            if ((int) (Math.random()) * 100 < 20) {
                rightType = RightType.BUG_CLOSE;
                roleType = RoleType.PM;
                bug.setSeverityType(SeverityType.CRITICAL);
            } else {
                if ((int) (Math.random()) * 100 < 40) {
                    rightType = RightType.BUG_EXPORT_PDF;
                    roleType = RoleType.TEST;
                    bug.setSeverityType(SeverityType.HIGH);
                } else {
                    if ((int) (Math.random()) * 100 < 60) {
                        rightType = RightType.BUG_MANAGEMENT;
                        roleType = RoleType.TM;
                        bug.setSeverityType(SeverityType.MEDIUM);
                    } else {
                        if ((int) (Math.random()) * 100 < 80) {
                            rightType = RightType.PERMISSION_MANAGEMENT;
                            roleType = RoleType.DEV;
                            bug.setSeverityType(SeverityType.LOW);
                        } else {
                            if ((int) (Math.random()) * 100 < 100) {
                                rightType = RightType.USER_MANAGEMENT;
                                roleType = RoleType.ADM;
                            }
                        }
                    }
                }
            }

            RightType rightType1 = RightType.BUG_MANAGEMENT;
            RoleType roleType1 = RoleType.DEV;

            if (rightType.equals(RightType.BUG_MANAGEMENT)) {
                rightType1 = RightType.USER_MANAGEMENT;
            }

            if (roleType.equals(RoleType.DEV)) {
                roleType1 = RoleType.ADM;
            }


            Right right = new Right();
            right.setType(rightType);
            Right right1 = new Right();
            right1.setType(rightType1);

            Collection<Right> rights = new LinkedList<>();
            rights.add(right);
            rights.add(right1);

            Role role = new Role();
            role.setRole(roleType);
            role.setRoleRights(rights);

            Role role1 = new Role();
            role1.setRole(roleType1);
            role1.setRoleRights(rights);

            Collection<Role> roles = new LinkedList<>();
            roles.add(role);
            roles.add(role1);

            e.setUsername(usernameGenerator.generateUsername(firstName, lastName, em));
            e.setPassword(passwordEncryptor.passwordEncryption(password));
            e.setLastName(lastName);
            e.setFirstName(firstName);
            e.setEmail(mail);

            bug.setCreatedBy(e);

            if (Math.random() > 0.5) {
                e.setMobileNumber(mobileNumber);
                bug.setAssignedTo(e);
                bug.setStatusType(StatusType.IN_PROGRESS);
            } else {
                e.setMobileNumber(germanNumber);
                bug.setStatusType(StatusType.NEW);
                bug.setSeverityType(SeverityType.LOW);
            }

            Notification n = new Notification();
            n.setNotificationType(NotificationType.BUG_STATUS_UPDATED);
            bug.setNotification(n);

            e.setUserRoles(roles);

            em.persist(e);
            em.persist(role);
            em.persist(role1);
            em.persist(right);
            em.persist(right1);
            em.persist(bug);
            em.persist(n);

        }
        utx.commit();

        em.clear();
    }

    @Override
    protected void internalClearData() {
        em.createQuery("delete from User ").executeUpdate();
        em.createQuery("delete from Role ").executeUpdate();
        em.createQuery("delete from Right").executeUpdate();
        em.createQuery("delete from Notification ").executeUpdate();
    }


}
