package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;

public class Category extends BaseRecord {

  private Category(long id, String name) {
    super(id, name);
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return name.get();
  }

  public static class Builder {

    private long id = -1L;
    private String name;

    private Builder() {
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Category build() {
      return new Category(id, Objects.requireNonNull(name));
    }
  }
}
