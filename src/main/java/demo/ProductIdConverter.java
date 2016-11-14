package demo;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

public class ProductIdConverter implements Converter {

  private static final long serialVersionUID = 1L;

  public ProductIdConverter() {}

  /**
   * Convert the databases' data representation of the value to the object's representation.
   */
  @Override
  public final Object convertDataValueToObjectValue(Object dataValue, Session session) {
    if (dataValue == null) {
      return null;
    } else if (dataValue instanceof Number) {
      return ProductId.create(((Number) dataValue).longValue());
    } else if (dataValue instanceof String) {
      // JUST for debugging
      return ProductId.create(Long.parseLong((String) dataValue));
    }
    throw new IllegalArgumentException(
        "dataValue must be of type Number, but is " + dataValue.getClass() + " with value " + dataValue);
  }

  /**
   * Convert the object's representation of the value to the databases' data representation.
   */
  @Override
  public final Object convertObjectValueToDataValue(Object objectValue, Session session) {
    if (objectValue == null) {
      return null;
    }
    if (!(objectValue instanceof ProductId)) {
      throw new IllegalArgumentException("Unexpected class of objectValue: " + objectValue.getClass());
    }
    Long result = ((ProductId) objectValue).getValue();
    return result;
  }

  @Override
  public final void initialize(DatabaseMapping mapping, Session session) {
    mapping.getField().setColumnDefinition("NUMBER");
    mapping.getField().setType(Long.class);
  }

  @Override
  public final boolean isMutable() {
    return false;
  }


}
