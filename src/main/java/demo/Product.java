package demo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

@Entity
@Table(name = "PRODUCT")
public class Product implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column( name = "ID", nullable = false)
  @Converter(name = "productIdConverter", converterClass = ProductIdConverter.class)
  @Convert("productIdConverter")
  private ProductId id;

  @Column(name = "NAME", nullable = false)
  private String name;

  public Product(ProductId id) {
    this.id = id;
  }

  @SuppressWarnings("unused")
  @Deprecated
  private Product() {
    // for JPA
  }

  public ProductId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
