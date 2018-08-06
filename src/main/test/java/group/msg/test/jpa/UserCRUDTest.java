package group.msg.test.jpa;

import group.msg.entities.User;
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

@RunWith(Arquillian.class)
public class UserCRUDTest extends JPABaseTest {

//public class UserCRUDTest {
  private static final int NUMBER_OF_ENTITIES = 5;
/*
  @Inject
  private EntityManager em;
*/
  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "JPAExamples.war")
            .addPackages(true,"group.msg")
            .addAsLibraries(MavenArtifactResolver.resolve(
                    "mysql:mysql-connector-java:5.1.46"))
            .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
            .addAsResource("../classes/META-INF/beans.xml", "META-INF/beans.xml");
  }

  @Test
  public void testCreateSimpleEntity() {
    System.out.println("Checking number of created entities...");

    Query q = em.createNamedQuery("JPAExample.findAll");
    Assert.assertEquals("Entities not found in the database!", NUMBER_OF_ENTITIES, q.getResultList().size());
  }


  @Override
  public void insertData() throws Exception {
    utx.begin();
    em.joinTransaction();
    System.out.println("Inserting records...");
    for (int i = 1; i <= NUMBER_OF_ENTITIES; i++) {
      User e = new User();
      if (i % 2 == 0) {
        e.setFirstName("Praf");
      } else {
        e.setLastName("Pulbere");
      }
      e.setEmail("wtv@gmail.com");
      em.persist(e);
    }
    utx.commit();

   em.clear();
  }
/*
  @Override
  protected void internalClearData() {
    em.createQuery("delete from User ").executeUpdate();
  }*/
}
