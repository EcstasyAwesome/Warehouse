package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class AbstractNamedRecord extends AbstractRecord {

  private final StringProperty name = new SimpleStringProperty();

  public String getName() {
    return name.get();
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public StringProperty nameProperty() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    var that = (AbstractNamedRecord) obj;
    return Objects.equals(name.get(), that.name.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(name.get());
  }

  @Override
  public String toString() {
    return name.get();
  }
}
