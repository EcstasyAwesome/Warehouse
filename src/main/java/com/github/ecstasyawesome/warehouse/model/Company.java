package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Company extends NamedRecord {

  private final StringProperty identifierCode;
  private final ObjectProperty<PersonType> personType;
  private final ObjectProperty<CompanyContact> companyContact;
  private final ObjectProperty<Address> address;

  public Company(Company instance) {
    this(instance.getId(), instance.getName(), instance.getIdentifierCode(),
        instance.getPersonType(), new CompanyContact(instance.getCompanyContact()),
        new Address(instance.getAddress()));
  }

  private Company(long id, String name, String identifierCode, PersonType personType,
      CompanyContact companyContact, Address address) {
    super(id, name);
    this.identifierCode = new SimpleStringProperty(identifierCode);
    this.personType = new SimpleObjectProperty<>(personType);
    this.companyContact = new SimpleObjectProperty<>(companyContact);
    this.address = new SimpleObjectProperty<>(address);
  }

  public static Builder getBuilder() {
    return new Builder();
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

  public CompanyContact getCompanyContact() {
    return companyContact.get();
  }

  public void setCompanyContact(CompanyContact companyContact) {
    this.companyContact.set(companyContact);
  }

  public ObjectProperty<CompanyContact> companyContactProperty() {
    return companyContact;
  }

  public Address getAddress() {
    return address.get();
  }

  public void setAddress(Address address) {
    this.address.set(address);
  }

  public ObjectProperty<Address> addressProperty() {
    return address;
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
           && Objects.equals(personType.get(), that.personType.get())
           && Objects.equals(companyContact.get(), that.companyContact.get())
           && Objects.equals(address.get(), that.address.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), identifierCode.get(), personType.get(),
        companyContact.get(), address.get());
  }

  public static class Builder {

    private long id = -1L;
    private String name;
    private String identifierCode;
    private PersonType personType;
    private CompanyContact companyContact;
    private Address address;

    private Builder() {
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

    public Builder setCompanyContact(CompanyContact companyContact) {
      this.companyContact = companyContact;
      return this;
    }

    public Builder setAddress(Address address) {
      this.address = address;
      return this;
    }

    public Company build() {
      return new Company(id,
          Objects.requireNonNull(name),
          Objects.requireNonNull(identifierCode),
          Objects.requireNonNull(personType),
          Objects.requireNonNull(companyContact),
          Objects.requireNonNull(address));
    }
  }
}
