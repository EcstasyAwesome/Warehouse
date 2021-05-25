package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends NamedRecord {

  private final StringProperty surname;
  private final StringProperty secondName;
  private final ObjectProperty<UserContact> userContact;
  private final ObjectProperty<UserSecurity> userSecurity;

  public User(User instance) {
    this(instance.getId(), instance.getSurname(), instance.getName(), instance.getSecondName(),
        new UserContact(instance.getUserContact()), new UserSecurity(instance.getUserSecurity()));
  }

  private User(long id, String surname, String name, String secondName, UserContact userContact,
      UserSecurity userSecurity) {
    super(id, name);
    this.surname = new SimpleStringProperty(surname);
    this.secondName = new SimpleStringProperty(secondName);
    this.userContact = new SimpleObjectProperty<>(userContact);
    this.userSecurity = new SimpleObjectProperty<>(userSecurity);
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public String getSurname() {
    return surname.get();
  }

  public void setSurname(String surname) {
    this.surname.set(surname);
  }

  public StringProperty surnameProperty() {
    return surname;
  }

  public String getSecondName() {
    return secondName.get();
  }

  public void setSecondName(String secondName) {
    this.secondName.set(secondName);
  }

  public StringProperty secondNameProperty() {
    return secondName;
  }

  public UserContact getUserContact() {
    return userContact.get();
  }

  public void setUserContact(UserContact userContact) {
    this.userContact.set(userContact);
  }

  public ObjectProperty<UserContact> userContactProperty() {
    return userContact;
  }

  public UserSecurity getUserSecurity() {
    return userSecurity.get();
  }

  public void setUserSecurity(UserSecurity userSecurity) {
    this.userSecurity.set(userSecurity);
  }

  public ObjectProperty<UserSecurity> userSecurityProperty() {
    return userSecurity;
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

    var that = (User) obj;
    return Objects.equals(surname.get(), that.surname.get())
           && Objects.equals(secondName.get(), that.secondName.get())
           && Objects.equals(userContact.get(), that.userContact.get())
           && Objects.equals(userSecurity.get(), that.userSecurity.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), surname.get(), secondName.get(), userContact.get(),
        userSecurity.get());
  }

  public static class Builder {

    private long id = -1L;
    private String surname;
    private String name;
    private String secondName;
    private UserContact userContact;
    private UserSecurity userSecurity;

    private Builder() {
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setSurname(String surname) {
      this.surname = surname;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setSecondName(String secondName) {
      this.secondName = secondName;
      return this;
    }

    public Builder setUserContact(UserContact userContact) {
      this.userContact = userContact;
      return this;
    }

    public Builder setUserSecurity(UserSecurity userSecurity) {
      this.userSecurity = userSecurity;
      return this;
    }

    public User build() {
      return new User(id,
          Objects.requireNonNull(surname),
          Objects.requireNonNull(name),
          Objects.requireNonNull(secondName),
          Objects.requireNonNull(userContact),
          Objects.requireNonNull(userSecurity));
    }
  }
}
