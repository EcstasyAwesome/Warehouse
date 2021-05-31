package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractPersonRecord;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer extends AbstractPersonRecord {

  private final StringProperty phone = new SimpleStringProperty();

  public Customer(Customer instance) {
    setId(instance.getId());
    setSurname(instance.getSurname());
    setName(instance.getName());
    setSecondName(instance.getSecondName());
    setPhone(instance.getPhone());
  }

  private Customer() {
  }

  public String getPhone() {
    return phone.get();
  }

  public void setPhone(String phone) {
    this.phone.set(phone);
  }

  public StringProperty phoneProperty() {
    return phone;
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
    var that = (Customer) obj;
    return Objects.equals(phone.get(), that.phone.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), phone.get());
  }

  public static class Builder {

    private long id = -1L;
    private String surname;
    private String name;
    private String secondName;
    private String phone;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setSurname(String surname) {
      this.surname = surname;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setSecondName(String secondName) {
      this.secondName = secondName;
      return this;
    }

    public Builder setPhone(String phone) {
      this.phone = phone;
      return this;
    }

    public Customer build() {
      var instance = new Customer();
      instance.setId(id);
      instance.setSurname(Objects.requireNonNull(surname));
      instance.setName(Objects.requireNonNull(name));
      instance.setSecondName(Objects.requireNonNull(secondName));
      instance.setPhone(Objects.requireNonNull(phone));
      return instance;
    }
  }
}
