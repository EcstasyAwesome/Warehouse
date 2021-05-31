package com.github.ecstasyawesome.warehouse.model;

import com.github.ecstasyawesome.warehouse.model.impl.Address;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class AbstractBusiness extends AbstractNamedRecord {

  private final ObjectProperty<BusinessContact> businessContact = new SimpleObjectProperty<>();
  private final ObjectProperty<Address> address = new SimpleObjectProperty<>();

  public BusinessContact getBusinessContact() {
    return businessContact.get();
  }

  public void setBusinessContact(BusinessContact businessContact) {
    this.businessContact.set(businessContact);
  }

  public ObjectProperty<BusinessContact> businessContactProperty() {
    return businessContact;
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

    var that = (AbstractBusiness) obj;
    return Objects.equals(businessContact.get(), that.businessContact.get())
           && Objects.equals(address.get(), that.address.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), businessContact.get(), address.get());
  }
}
