package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public abstract class Record {

  protected final LongProperty id;

  protected Record(long id) {
    this.id = new SimpleLongProperty(id);
  }

  public long getId() {
    return id.get();
  }

  public void setId(long id) {
    this.id.set(id);
  }

  public LongProperty idProperty() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var that = (Record) obj;
    return id.get() == that.id.get();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id.get());
  }
}
