package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractContact;
import com.github.ecstasyawesome.warehouse.model.Recoverable;
import java.util.Objects;

public class PersonContact extends AbstractContact implements Recoverable<PersonContact> {

  public PersonContact(PersonContact instance) {
    setId(instance.getId());
    setPhone(instance.getPhone());
    setEmail(instance.getEmail());
  }

  private PersonContact() {
  }

  @Override
  public void recover(PersonContact instance) {
    setId(instance.getId());
    setPhone(instance.getPhone());
    setEmail(instance.getEmail());
  }

  public static class Builder {

    private long id = -1L;
    private String phone;
    private String email;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
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

    public PersonContact build() {
      var instance = new PersonContact();
      instance.setId(id);
      instance.setPhone(Objects.requireNonNull(phone));
      instance.setEmail(email);
      return instance;
    }
  }
}
