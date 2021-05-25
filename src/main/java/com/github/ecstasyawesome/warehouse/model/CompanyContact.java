package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CompanyContact extends ContactRecord {

  private final StringProperty extraPhone;

  public CompanyContact(CompanyContact instance) {
    this(instance.getId(), instance.getPhone(), instance.getExtraPhone(), instance.getEmail());
  }

  private CompanyContact(long id, String phone, String extraPhone, String email) {
    super(id, phone, email);
    this.extraPhone = new SimpleStringProperty(extraPhone);
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public String getExtraPhone() {
    return extraPhone.get();
  }

  public void setExtraPhone(String extraPhone) {
    this.extraPhone.set(extraPhone);
  }

  public StringProperty extraPhoneProperty() {
    return extraPhone;
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

    var that = (CompanyContact) obj;
    return Objects.equals(extraPhone.get(), that.extraPhone.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), extraPhone.get());
  }

  public static class Builder {

    private long id = -1L;
    private String phone;
    private String extraPhone;
    private String email;

    private Builder() {
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setPhone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder setExtraPhone(String extraPhone) {
      this.extraPhone = extraPhone;
      return this;
    }

    public Builder setEmail(String email) {
      this.email = email;
      return this;
    }

    public CompanyContact build() {
      return new CompanyContact(id, Objects.requireNonNull(phone), extraPhone, email);
    }
  }
}
