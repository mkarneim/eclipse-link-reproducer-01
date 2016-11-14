package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.BeforeClass;

public class IntegrationTestBase extends Assertions {
  private static final String DB_DIRECTORY = "test-hsqldb";

  private final EntityManagerFactory emFactory;
  private final String dbFilename;

  private long productIdNextVal = 1L;
  private EntityManager em;

  public IntegrationTestBase() {
    dbFilename = UUID.randomUUID().toString();
    Properties properties = new Properties();
    properties.setProperty("javax.persistence.jdbc.driver", "org.hsqldb.jdbc.JDBCDriver");
    properties.setProperty("javax.persistence.jdbc.url", "jdbc:hsqldb:file:" + DB_DIRECTORY + "/" + dbFilename
        + ";create=true;hsqldb.default_table_type=CACHED;hsqldb.write_delay=false;sql.syntax_ora=true;");
    properties.setProperty("javax.persistence.jdbc.user", "sa");
    properties.setProperty("javax.persistence.jdbc.password", "");
    emFactory = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
  }

  public EntityManager newEntityManager() {
    if (em == null) {
      em = emFactory.createEntityManager();
    }
    return em;
  }

  public Product newProduct() {
    ProductId id = ProductId.create(productIdNextVal++);
    Product result = new Product(id);
    result.setName("some name");
    return result;
  }

  @BeforeClass
  public static void clearDatabases() throws IOException {
    FileUtils.deleteDirectory(new File(DB_DIRECTORY));
  }

  @After
  public void closeEntityManagers() throws IOException {
    try {
      if (em.getTransaction().isActive()) {
        if ( em.getTransaction().getRollbackOnly()) {
          em.getTransaction().rollback();
        } else {
          em.getTransaction().commit();
        }
      }
    } finally {
      try {
        em.getTransaction().begin();
        em.createNativeQuery("SHUTDOWN").executeUpdate();
        em.getTransaction().commit();
        em.close();
        waitForShutdownCompleted(20000L);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void waitForShutdownCompleted(long timeoutMillis) throws IOException {
    long started = System.currentTimeMillis();
    File propertiesFile = new File(DB_DIRECTORY + "/" + dbFilename + ".properties");
    Properties props = new Properties();
    boolean modified = true;
    while (modified) {
      if (started + timeoutMillis < System.currentTimeMillis()) {
        throw new IllegalStateException(String.format("Database shutdown timeout (%s ms) reached for DB %s!",
            timeoutMillis, propertiesFile.getParentFile().getName()));
      }
      try (FileInputStream in = new FileInputStream(propertiesFile)) {
        props.load(in);
      }
      String modifiedStr = props.getProperty("modified", "yes");
      modified = modifiedStr.equals("yes");
      if (modified) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e1) {
          // ignore
        }
      }
    }
  }

}
