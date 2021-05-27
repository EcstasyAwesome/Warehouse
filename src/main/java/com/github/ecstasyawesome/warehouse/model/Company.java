package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Company extends AbstractBusiness {

  private final StringProperty identifierCode = new SimpleStringProperty();
  private final ObjectProperty<PersonType> personType = new SimpleObjectProperty<>();

  public Company(Company instance) {
    setId(instance.getId());
    setName(instance.getName());
    setAddress(new Address(instance.getAddress()));
    setBusinessContact(new BusinessContact(instance.getBusinessContact()));
    setIdentifierCode(instance.getIdentifierCode());
    setPersonType(instance.getPersonType());
  }

  private Company() {
  }

  public String getIdentifierCode() {
    return identifierCode.get();
  }

  public void setIdentifierCode(String identifierCode) {
    this.identifierCode.set(identifierCode);
  }

  public StringProperty identifierCodeProperty() {
    return identifierCode;
  }

  public PersonType getPersonType() {
    return personType.get();
  }

  public void setPersonType(PersonType personType) {
    this.personType.set(personType);
  }

  public ObjectProperty<PersonType> personTypeProperty() {
    return personType;
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

    var that = (Company) obj;
    return Objects.equals(identifierCode.get(), that.identifierCode.get())
           && Objects.equals(personType.get(), that.personType.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), identifierCode.get(), personType.get());
  }

  public static class Builder {

    private long id = -1L;
    private String name;
    private String identifierCode;
    private PersonType personType;
    private BusinessContact businessContact;
    private Address address;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setIdentifierCode(String identifierCode) {
      this.identifierCode = identifierCode;
      return this;
    }

    public Builder setPersonType(PersonType personType) {
      this.personType = personType;
      return this;
    }

    public Builder setBusinessContact(BusinessContact businessContact) {
      this.businessContact = businessContact;
      return this;
    }

    public Builder setAddress(Address address) {
      this.address = address;
      return this;
    }

    public Company build() {
      var instance = new Company();
      instance.setId(id);
      instance.setName(Objects.requireNonNull(name));
      instance.setAddress(Objects.requireNonNull(address));
      instance.setBusinessContact(Objects.requireNonNull(businessContact));
      instance.setIdentifierCode(Objects.requireNonNull(identifierCode));
      instance.setPersonType(Objects.requireNonNull(personType));
      return instance;
    }
  }
}
