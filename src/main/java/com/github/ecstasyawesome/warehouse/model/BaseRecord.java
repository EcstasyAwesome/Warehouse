package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;

public abstract class BaseRecord {

  protected long id;
  protected String name;

  protected BaseRecord(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
    return this.id == that.id && this.name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
