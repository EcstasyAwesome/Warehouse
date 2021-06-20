package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class AbstractContact extends AbstractRecord {

  private final StringProperty phone = new SimpleStringProperty();
  private final StringProperty email = new SimpleStringProperty();

  public String getPhone() {
    return phone.get();
  }

  public void setPhone(String phone) {
    this.phone.set(phone);
  }

  public StringProperty phoneProperty() {
    return phone;
  }

  public String getEmail() {
    return email.get();
  }

  public void setEmail(String email) {
    this.email.set(email);
  }

  public StringProperty emailProperty() {
    return email;
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

    var that = (AbstractContact) obj;
    return Objects.equals(getPhone(), that.getPhone())
           && Objects.equals(getEmail(), that.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getPhone(), getEmail());
  }
}
