package org.apache.olingo.jpa.processor.core.serializer;

import java.util.List;

import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.uri.UriHelper;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceProperty;

public abstract class JPASerializePrimitiveAbstract implements JPASerializer {
  protected final static char PATH_SEPERATOR = '/';
  protected final ServiceMetadata serviceMetadata;
  protected final UriInfo uriInfo;

  public JPASerializePrimitiveAbstract(final ServiceMetadata serviceMetadata, final UriHelper uriHelper,
      final UriInfo uriInfo) {
    super();
    this.serviceMetadata = serviceMetadata;
    this.uriInfo = uriInfo;
  }

  protected final JPAPrimitivePropertyInfo determinePrimitiveProperty(final EntityCollection result,
      final List<UriResource> uriResources) {
    Property property = null;
    Object value = null;

    StringBuffer path = new StringBuffer();

    for (final Property item : result.getEntities().get(0).getProperties()) {
      if (partOfPath(item, uriResources)) {
        property = item;
        boolean found = false;
        while (!found) {
          path.append(property.getName());
          if (property.getValue() instanceof ComplexValue) {
            value = property.getValue();
            property = ((ComplexValue) value).getValue().get(0);

            path.append(PATH_SEPERATOR);
          } else {
            found = true;
          }
        }
        break;
      }
    }
    return new JPAPrimitivePropertyInfo(path.toString(), property);
  }

  private boolean partOfPath(Property item, List<UriResource> uriResources) {
    for (UriResource resource : uriResources) {
      if (resource instanceof UriResourceProperty
          && ((UriResourceProperty) resource).getProperty().getName().equals(item.getName()))
        return true;
    }
    return false;
  }

}