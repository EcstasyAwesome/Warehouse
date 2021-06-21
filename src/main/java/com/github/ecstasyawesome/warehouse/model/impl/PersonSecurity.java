package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractRecord;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.Recoverable;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PersonSecurity extends AbstractRecord implements Recoverable<PersonSecurity> {

  private final StringProperty login = new SimpleStringProperty();
  private final StringProperty password = new SimpleStringProperty();
  private final ObjectProperty<Access> access = new SimpleObjectProperty<>();

  public PersonSecurity(PersonSecurity instance) {
    setId(instance.getId());
    setLogin(instance.getLogin());
    setPassword(instance.getPassword());
    setAccess(instance.getAccess());
  }

  private PersonSecurity() {
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
  public void recover(PersonSecurity instance) {
    setId(instance.getId());
    setLogin(instance.getLogin());
    setPassword(instance.getPassword());
    setAccess(instance.getAccess());
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

    var that = (PersonSecurity) obj;
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

    public static Builder create() {
      return new Builder();
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

    public PersonSecurity build() {
      var instance = new PersonSecurity();
      instance.setId(id);
      instance.setLogin(Objects.requireNonNull(login));
      instance.setPassword(Objects.requireNonNull(password));
      instance.setAccess(Objects.requireNonNull(access));
      return instance;
    }
  }
}
