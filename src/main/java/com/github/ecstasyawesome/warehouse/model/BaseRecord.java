package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class BaseRecord {

  protected final LongProperty id;
  protected final StringProperty name;

  protected BaseRecord(long id, String name) {
    this.id = new SimpleLongProperty(id);
    this.name = new SimpleStringProperty(name);
  }

  public long getId() {
    return id.get();
  }

  public void setId(long id) {
    this.id.set(id);
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
    var that = (BaseRecord) obj;
    return this.id.get() == that.id.get() && this.name.get().equals(that.name.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id.get(), name.get());
  }
}
