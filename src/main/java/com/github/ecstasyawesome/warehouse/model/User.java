package com.github.ecstasyawesome.warehouse.model;

import com.github.ecstasyawesome.warehouse.core.Access;
import java.util.Objects;

public class User extends BaseRecord {

  private String surname;
  private String secondName;
  private String phone;
  private String login;
  private String password;
  private Access access;

  private User(long id, String surname, String name, String secondName, String phone, String login,
      String password, Access access) {
    super(id, name);
    this.surname = surname;
    this.secondName = secondName;
    this.phone = phone;
    this.login = login;
    this.password = password;
    this.access = access;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Access getAccess() {
    return access;
  }

  public void setAccess(Access access) {
    this.access = access;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), surname, secondName, phone, login, password, access);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    var that = (User) obj;
    return this.surname.equals(that.surname)
           && this.secondName.equals(that.secondName)
           && this.phone.equals(that.phone)
           && this.login.equals(that.login)
           && this.password.equals(that.password)
           && this.access.equals(that.access);
  }

  public static class Builder {

    private long id = -1L;
    private String surname;
    private String name;
    private String secondName;
    private String phone;
    private String login;
    private String password;
    private Access access;

    private Builder() {
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder surname(String surname) {
      this.surname = surname;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder secondName(String secondName) {
      this.secondName = secondName;
      return this;
    }

    public Builder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder login(String login) {
      this.login = login;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder access(Access access) {
      this.access = access;
      return this;
    }

    public User build() {
      return new User(
          id,
          Objects.requireNonNull(surname),
          Objects.requireNonNull(name),
          Objects.requireNonNull(secondName),
          Objects.requireNonNull(phone),
          Objects.requireNonNull(login),
          Objects.requireNonNull(password),
          Objects.requireNonNull(access));
    }
  }
}
