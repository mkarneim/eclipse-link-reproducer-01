package demo;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Test;

public class ProductTest extends IntegrationTestBase {

  @Test
  public void test_1() {
    EntityManager em = newEntityManager();
    em.getTransaction().begin();

    // Given:
    Product prod = newProduct();

    // When:
    em.persist(prod);
    em.flush();

    // Then:
    Product actual = em.find(Product.class, prod.getId());

    assertThat(actual).isSameAs(prod); // <-- SUCCEEDS

    em.getTransaction().commit();
  }

  @Test
  public void test_2() {
    EntityManager em = newEntityManager();
    em.getTransaction().begin();

    // Given:
    Product prod = newProduct();

    // When:
    em.persist(prod);
    em.flush();

    // Then:
    Product actual =
        em.createQuery("SELECT x from Product x where x.id = " + prod.getId(), Product.class).getSingleResult();

    assertThat(actual).isSameAs(prod); // <-- Fails

    em.getTransaction().commit();
  }

  @Test
  public void test_3() {
    EntityManager em = newEntityManager();
    em.getTransaction().begin();

    // Given:
    Product prod = newProduct();

    // When:
    em.persist(prod);
    em.flush();

    // Then:
    TypedQuery<Product> query = em.createQuery("SELECT x from Product x where x.id = :id", Product.class);
    query.setParameter("id", prod.getId());
    Product actual = query.getSingleResult();

    assertThat(actual).isSameAs(prod); // <-- Fails

    em.getTransaction().commit();
  }

  @Test
  public void test_4() {
    EntityManager em = newEntityManager();
    em.getTransaction().begin();

    // Given:
    Product prod = newProduct();

    // When:
    em.persist(prod);
    em.flush();

    // Then:
    TypedQuery<Product> query = em.createQuery("SELECT x from Product x where x.name = :name", Product.class);
    query.setParameter("name", prod.getName());
    Product actual = query.getSingleResult();

    assertThat(actual).isSameAs(prod); // <-- Fails

    em.getTransaction().commit();
  }

}
