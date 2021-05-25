package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Product extends NamedRecord {

  private final ObjectProperty<Category> category;
  private final ObjectProperty<Unit> unit;

  public Product(Product instance) {
    this(instance.getId(), instance.getName(), new Category(instance.getCategory()),
        instance.getUnit());
  }

  private Product(long id, String name, Category category, Unit unit) {
    super(id, name);
    this.category = new SimpleObjectProperty<>(category);
    this.unit = new SimpleObjectProperty<>(unit);
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public Category getCategory() {
    return category.get();
  }

  public void setCategory(Category category) {
    this.category.set(category);
  }

  public ObjectProperty<Category> categoryProperty() {
    return category;
  }

  public Unit getUnit() {
    return unit.get();
  }

  public void setUnit(Unit unit) {
    this.unit.set(unit);
  }

  public ObjectProperty<Unit> unitProperty() {
    return unit;
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

    var that = (Product) obj;
    return Objects.equals(category.get(), that.category.get())
           && Objects.equals(unit.get(), that.unit.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), category.get(), unit.get());
  }

  public static class Builder {

    private long id = -1L;
    private String name;
    private Category category;
    private Unit unit;

    private Builder() {
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setCategory(Category category) {
      this.category = category;
      return this;
    }

    public Builder setUnit(Unit unit) {
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
