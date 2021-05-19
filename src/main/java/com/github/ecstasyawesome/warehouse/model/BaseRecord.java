package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public abstract class BaseRecord {

  protected final LongProperty id;

  protected BaseRecord(long id) {
    this.id = new SimpleLongProperty(id);
  }

  public long getId() {
    return id.get();
  }

  public void setId(long id) {
    this.id.set(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    var that = (BaseRecord) obj;
    return this.id.get() == that.id.get();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id.get());
  }
}
