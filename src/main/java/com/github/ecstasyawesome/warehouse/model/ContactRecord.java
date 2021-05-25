package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class ContactRecord extends Record {

  protected final StringProperty phone;
  protected final StringProperty email;

  protected ContactRecord(long id, String phone, String email) {
    super(id);
    this.phone = new SimpleStringProperty(phone);
    this.email = new SimpleStringProperty(email);
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

    var that = (ContactRecord) obj;
    return Objects.equals(phone.get(), that.phone.get())
           && Objects.equals(email.get(), that.email.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), phone.get(), email.get());
  }
}
