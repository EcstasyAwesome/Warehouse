package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;

public class Product extends BaseRecord {

  private Category category;
  private Unit unit;

  private Product(long id, String name, Category category, Unit unit) {
    super(id, name);
    this.category = category;
    this.unit = unit;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
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

    var that = (Product) obj;
    return this.category.equals(that.category) && this.unit == that.unit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), category, unit);
  }

  public static class Builder {

    private long id = -1L;
    private String name;
    private Category category;
    private Unit unit;

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

    public Builder category(Category category) {
      this.category = category;
      return this;
    }

    public Builder unit(Unit unit) {
      this.unit = unit;
      return this;
    }

    public Product build() {
      return new Product(
          id,
          Objects.requireNonNull(name),
          Objects.requireNonNull(category),
          Objects.requireNonNull(unit));
    }
  }
}
