package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.Record;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserSecurity extends Record {

  private final StringProperty login;
  private final StringProperty password;
  private final ObjectProperty<Access> access;

  public UserSecurity(UserSecurity instance) {
    this(instance.getId(), instance.getLogin(), instance.getPassword(), instance.getAccess());
  }

  private UserSecurity(long id, String login, String password, Access access) {
    super(id);
    this.login = new SimpleStringProperty(login);
    this.password = new SimpleStringProperty(password);
    this.access = new SimpleObjectProperty<>(access);
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public String getLogin() {
    return login.get();
  }

  public void setLogin(String login) {
    this.login.set(login);
  }

  public StringProperty loginProperty() {
    return login;
  }

  public String getPassword() {
    return password.get();
  }

  public void setPassword(String password) {
    this.password.set(password);
  }

  public StringProperty passwordProperty() {
    return password;
  }

  public Access getAccess() {
    return access.get();
  }

  public void setAccess(Access access) {
    this.access.set(access);
  }

  public ObjectProperty<Access> accessProperty() {
    return access;
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
    var that = (UserSecurity) obj;
    return Objects.equals(login.get(), that.login.get())
           && Objects.equals(password.get(), that.password.get())
           && Objects.equals(access.get(), that.access.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), login.get(), password.get(), access.get());
  }

  public static class Builder {

    private long id = -1L;
    private String login;
    private String password;
    private Access access;

    private Builder() {
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setLogin(String login) {
      this.login = login;
      return this;
    }

    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    public Builder setAccess(Access access) {
      this.access = access;
      return this;
    }

    public UserSecurity build() {
      return new UserSecurity(id,
          Objects.requireNonNull(login),
          Objects.requireNonNull(password),
          Objects.requireNonNull(access));
    }
  }
}
