package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;

public class ProductProvider extends StorageRecord {

  public ProductProvider(ProductProvider instance) {
    this(instance.getId(), instance.getName(), new CompanyContact(instance.getCompanyContact()),
        new Address(instance.getAddress()));
  }

  private ProductProvider(long id, String name, CompanyContact companyContact, Address address) {
    super(id, name, companyContact, address);
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public static class Builder {

    private long id = -1L;
    private String name;
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

    public Builder setCompanyContact(CompanyContact companyContact) {
      this.companyContact = companyContact;
      return this;
    }

    public Builder setAddress(Address address) {
      this.address = address;
      return this;
    }

    public ProductProvider build() {
      return new ProductProvider(id,
          Objects.requireNonNull(name),
          Objects.requireNonNull(companyContact),
          Objects.requireNonNull(address));
    }
  }
}
