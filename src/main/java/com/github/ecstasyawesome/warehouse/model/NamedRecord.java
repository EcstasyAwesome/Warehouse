package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class NamedRecord extends BaseRecord {

  protected final StringProperty name;

  protected NamedRecord(long id, String name) {
    super(id);
    this.name = new SimpleStringProperty(name);
  }

  public String getName() {
    return name.get();
  }

  public void setName(String name) {
    this.name.set(name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    var that = (NamedRecord) obj;
    return this.name.get().equals(that.name.get());
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
