package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractNamedRecord;
import com.github.ecstasyawesome.warehouse.model.Recoverable;
import com.github.ecstasyawesome.warehouse.model.Unit;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Product extends AbstractNamedRecord implements Recoverable<Product> {

  private final ObjectProperty<Category> category = new SimpleObjectProperty<>();
  private final ObjectProperty<Unit> unit = new SimpleObjectProperty<>();

  public Product(Product instance) {
    setId(instance.getId());
    setName(instance.getName());
    setCategory(new Category(instance.getCategory()));
    setUnit(instance.getUnit());
  }

  private Product() {
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
  public void recover(Product instance) {
    setId(instance.getId());
    setName(instance.getName());
    setUnit(instance.getUnit());
    getCategory().recover(instance.getCategory());
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
    return Objects.equals(getCategory(), that.getCategory())
           && Objects.equals(getUnit(), that.getUnit());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getCategory(), getUnit());
  }

  public static class Builder {

    protected long id = -1L;
    private String name;
    private Category category;
    private Unit unit;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
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
      var instance = new Product();
      instance.setId(id);
      instance.setName(Objects.requireNonNull(name));
      instance.setCategory(Objects.requireNonNull(category));
      instance.setUnit(Objects.requireNonNull(unit));
      return instance;
    }
  }
}
