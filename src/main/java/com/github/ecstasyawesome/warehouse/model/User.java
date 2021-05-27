package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends AbstractNamedRecord {

  private final StringProperty surname = new SimpleStringProperty();
  private final StringProperty secondName = new SimpleStringProperty();
  private final ObjectProperty<PersonContact> personContact = new SimpleObjectProperty<>();
  private final ObjectProperty<PersonSecurity> personSecurity = new SimpleObjectProperty<>();

  public User(User instance) {
    setId(instance.getId());
    setSurname(instance.getSurname());
    setName(instance.getName());
    setSecondName(instance.getSecondName());
    setPersonContact(new PersonContact(instance.getPersonContact()));
    setPersonSecurity(new PersonSecurity(instance.getPersonSecurity()));
  }

  private User() {
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

  public PersonContact getPersonContact() {
    return personContact.get();
  }

  public void setPersonContact(PersonContact personContact) {
    this.personContact.set(personContact);
  }

  public ObjectProperty<PersonContact> personContactProperty() {
    return personContact;
  }

  public PersonSecurity getPersonSecurity() {
    return personSecurity.get();
  }

  public void setPersonSecurity(PersonSecurity personSecurity) {
    this.personSecurity.set(personSecurity);
  }

  public ObjectProperty<PersonSecurity> personSecurityProperty() {
    return personSecurity;
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
           && Objects.equals(personContact.get(), that.personContact.get())
           && Objects.equals(personSecurity.get(), that.personSecurity.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), surname.get(), secondName.get(), personContact.get(),
        personSecurity.get());
  }

  public static class Builder {

    private long id = -1L;
    private String surname;
    private String name;
    private String secondName;
    private PersonContact personContact;
    private PersonSecurity personSecurity;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
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

    public Builder setUserContact(PersonContact personContact) {
      this.personContact = personContact;
      return this;
    }

    public Builder setUserSecurity(PersonSecurity personSecurity) {
      this.personSecurity = personSecurity;
      return this;
    }

    public User build() {
      var instance = new User();
      instance.setId(id);
      instance.setSurname(Objects.requireNonNull(surname));
      instance.setName(Objects.requireNonNull(name));
      instance.setSecondName(Objects.requireNonNull(secondName));
      instance.setPersonContact(Objects.requireNonNull(personContact));
      instance.setPersonSecurity(Objects.requireNonNull(personSecurity));
      return instance;
    }
  }
}
