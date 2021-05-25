package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.ContactRecord;
import java.util.Objects;

public class UserContact extends ContactRecord {

  public UserContact(UserContact instance) {
    this(instance.getId(), instance.getPhone(), instance.getEmail());
  }

  private UserContact(long id, String phone, String email) {
    super(id, phone, email);
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public static class Builder {

    private long id = -1L;
    private String phone;
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

    public Builder setEmail(String email) {
      this.email = email;
      return this;
    }

    public UserContact build() {
      return new UserContact(id, Objects.requireNonNull(phone), email);
    }
  }
}
