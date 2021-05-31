package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractBusiness;
import java.util.Objects;

public class ProductProvider extends AbstractBusiness {

  public ProductProvider(ProductProvider instance) {
    setId(instance.getId());
    setName(instance.getName());
    setAddress(new Address(instance.getAddress()));
    setBusinessContact(new BusinessContact(instance.getBusinessContact()));
  }

  private ProductProvider() {
  }

  public static class Builder {

    private long id = -1L;
    private String name;
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

    public Builder setBusinessContact(BusinessContact businessContact) {
      this.businessContact = businessContact;
      return this;
    }

    public Builder setAddress(Address address) {
      this.address = address;
      return this;
    }

    public ProductProvider build() {
      var instance = new ProductProvider();
      instance.setId(id);
      instance.setName(Objects.requireNonNull(name));
      instance.setAddress(Objects.requireNonNull(address));
      instance.setBusinessContact(Objects.requireNonNull(businessContact));
      return instance;
    }
  }
}
