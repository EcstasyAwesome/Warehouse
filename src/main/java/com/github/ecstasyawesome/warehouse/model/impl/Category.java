package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractNamedRecord;
import com.github.ecstasyawesome.warehouse.model.Recoverable;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category extends AbstractNamedRecord implements Recoverable<Category> {

  private final StringProperty description = new SimpleStringProperty();

  public Category(Category instance) {
    setId(instance.getId());
    setName(instance.getName());
    setDescription(instance.getDescription());
  }

  private Category() {
  }

  public String getDescription() {
    return description.get();
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  @Override
  public void recover(Category instance) {
    setId(instance.getId());
    setName(instance.getName());
    setDescription(instance.getDescription());
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

    var that = (Category) obj;
    return Objects.equals(description.get(), that.description.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), description.get());
  }

  public static class Builder {

    private long id = -1L;
    private String name;
    private String description;

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

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Category build() {
      var instance = new Category();
      instance.setId(id);
      instance.setName(Objects.requireNonNull(name));
      instance.setDescription(description);
      return instance;
    }
  }
}
