package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.StorageRecord;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProductStorage extends StorageRecord {

  private final ObjectProperty<Company> company;

  public ProductStorage(ProductStorage instance) {
    this(instance.getId(), instance.getName(), new CompanyContact(instance.getCompanyContact()),
        new Address(instance.getAddress()), new Company(instance.getCompany()));
  }

  private ProductStorage(long id, String name, CompanyContact companyContact, Address address,
      Company company) {
    super(id, name, companyContact, address);
    this.company = new SimpleObjectProperty<>(company);
  }

  public static Builder getBuilder() {
    return new Builder();
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
    private CompanyContact companyContact;
    private Address address;
    private Company company;

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

    public Builder setCompanyContact(CompanyContact companyContact) {
      this.companyContact = companyContact;
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
      return new ProductStorage(id,
          Objects.requireNonNull(name),
          Objects.requireNonNull(companyContact),
          Objects.requireNonNull(address),
          Objects.requireNonNull(company));
    }
  }
}
