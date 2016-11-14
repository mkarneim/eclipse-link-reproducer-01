package demo;

import java.io.Serializable;

import com.google.common.primitives.Ints;

public class ProductId extends Number implements Serializable, Comparable<ProductId> {

  private static final long serialVersionUID = 1L;

  public static ProductId create(final Long value) {
    return value == null ? null : new ProductId(value);
  }

  public static ProductId create(final long value) {
    return new ProductId(value);
  }

  private long value;

  private ProductId(long value) {
    this.value = value;
  }

  public long getValue() {
    return value;
  }

  @Override
  public int compareTo(ProductId o) {
    return Long.compare(value, o.value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public int hashCode() {
    return Long.hashCode(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ProductId other = (ProductId) obj;
    if (value != other.value) return false;
    return true;
  }

  @Override
  public int intValue() {
    return Ints.checkedCast(value);
  }

  public long longValue() {
    return value;
  }

  @Override
  public float floatValue() {
    return value;
  }

  @Override
  public double doubleValue() {
    return value;
  }

}
