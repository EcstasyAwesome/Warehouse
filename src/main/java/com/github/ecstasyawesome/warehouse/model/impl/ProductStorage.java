package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractBusiness;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProductStorage extends AbstractBusiness {

  private final ObjectProperty<Company> company = new SimpleObjectProperty<>();

  public ProductStorage(ProductStorage instance) {
    setId(instance.getId());
    setName(instance.getName());
    setAddress(new Address(instance.getAddress()));
    setBusinessContact(new BusinessContact(instance.getBusinessContact()));
    setCompany(new Company(instance.getCompany()));
  }

  private ProductStorage() {
  }

  public Company getCompany() {
    return company.get();
  }

  public void setCompany(Company company) {
    this.company.set(company);
  }

  public ObjectProperty<Company> companyProperty() {
    return company;
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

    var that = (ProductStorage) obj;
    return Objects.equals(company.get(), that.company.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), company.get());
  }

  public static class Builder {

    private long id = -1L;
    private String name;
    private BusinessContact businessContact;
    private Address address;
    private Company company;

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

    public Builder setBusinessContact(BusinessContact businessContact) {
      this.businessContact = businessContact;
      return this;
    }

    public Builder setAddress(Address address) {
      this.address = address;
      return this;
    }

    public Builder setCompany(Company company) {
      this.company = company;
      return this;
    }

    public ProductStorage build() {
      var instance = new ProductStorage();
      instance.setId(id);
      instance.setName(Objects.requireNonNull(name));
      instance.setAddress(Objects.requireNonNull(address));
      instance.setBusinessContact(Objects.requireNonNull(businessContact));
      instance.setCompany(Objects.requireNonNull(company));
      return instance;
    }
  }
}
