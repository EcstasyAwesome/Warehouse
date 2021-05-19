package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends NamedRecord {

  private final StringProperty surname;
  private final StringProperty secondName;
  private final StringProperty phone;
  private final StringProperty login;
  private final StringProperty password;
  private final ObjectProperty<Access> access;

  private User(long id, String surname, String name, String secondName, String phone, String login,
      String password, Access access) {
    super(id, name);
    this.surname = new SimpleStringProperty(surname);
    this.secondName = new SimpleStringProperty(secondName);
    this.phone = new SimpleStringProperty(phone);
    this.login = new SimpleStringProperty(login);
    this.password = new SimpleStringProperty(password);
    this.access = new SimpleObjectProperty<>(access);
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getSurname() {
    return surname.get();
  }

  public void setSurname(String surname) {
    this.surname.set(surname);
  }

  public String getSecondName() {
    return secondName.get();
  }

  public void setSecondName(String secondName) {
    this.secondName.set(secondName);
  }

  public String getPhone() {
    return phone.get();
  }

  public void setPhone(String phone) {
    this.phone.set(phone);
  }

  public String getLogin() {
    return login.get();
  }

  public void setLogin(String login) {
    this.login.set(login);
  }

  public String getPassword() {
    return password.get();
  }

  public void setPassword(String password) {
    this.password.set(password);
  }

  public Access getAccess() {
    return access.get();
  }

  public void setAccess(Access access) {
    this.access.set(access);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), surname.get(), secondName.get(), phone.get(), login.get(),
        password.get(), access.get());
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
    return this.surname.get().equals(that.surname.get())
           && this.secondName.get().equals(that.secondName.get())
           && this.phone.get().equals(that.phone.get())
           && this.login.get().equals(that.login.get())
           && this.password.get().equals(that.password.get())
           && this.access.get().equals(that.access.get());
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
