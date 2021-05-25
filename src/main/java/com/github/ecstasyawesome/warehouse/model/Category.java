package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category extends NamedRecord {

  private final StringProperty description;

  public Category(Category instance) {
    this(instance.getId(), instance.getName(), instance.getDescription());
  }

  private Category(long id, String name, String description) {
    super(id, name);
    this.description = new SimpleStringProperty(description);
  }

  public static Builder builder() {
    return new Builder();
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

  public static class Builder {

    private long id = -1L;
    private String name;
    private String description;

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

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Category build() {
      return new Category(id, Objects.requireNonNull(name), description);
    }
  }
}
