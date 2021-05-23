package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;

public class UserContact extends ContactRecord {

  public UserContact(UserContact instance) {
    this(instance.getId(), instance.getPhone(), instance.getEmail());
  }

  private UserContact(long id, String phone, String email) {
    super(id, phone, email);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private long id = -1L;
    private String phone;
    private String email;

    private Builder() {
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public UserContact build() {
      return new UserContact(id, Objects.requireNonNull(phone), email);
    }
  }
}
