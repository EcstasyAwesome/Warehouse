package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class StorageRecord extends NamedRecord {

  protected final ObjectProperty<CompanyContact> companyContact;
  protected final ObjectProperty<Address> address;

  protected StorageRecord(long id, String name, CompanyContact companyContact, Address address) {
    super(id, name);
    this.companyContact = new SimpleObjectProperty<>(companyContact);
    this.address = new SimpleObjectProperty<>(address);
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

    var that = (StorageRecord) obj;
    return Objects.equals(companyContact.get(), that.companyContact.get())
           && Objects.equals(address.get(), that.address.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), companyContact.get(), address.get());
  }
}
