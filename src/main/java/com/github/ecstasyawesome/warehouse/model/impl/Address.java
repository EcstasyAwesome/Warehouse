package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractRecord;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address extends AbstractRecord {

  private final StringProperty region = new SimpleStringProperty();
  private final StringProperty town = new SimpleStringProperty();
  private final StringProperty street = new SimpleStringProperty();
  private final StringProperty number = new SimpleStringProperty();

  public Address(Address instance) {
    setId(instance.getId());
    setRegion(instance.getRegion());
    setTown(instance.getTown());
    setStreet(instance.getStreet());
    setNumber(instance.getNumber());
  }

  private Address() {
  }

  public String getRegion() {
    return region.get();
  }

  public void setRegion(String region) {
    this.region.set(region);
  }

  public StringProperty regionProperty() {
    return region;
  }

  public String getTown() {
    return town.get();
  }

  public void setTown(String town) {
    this.town.set(town);
  }

  public StringProperty townProperty() {
    return town;
  }

  public String getStreet() {
    return street.get();
  }

  public void setStreet(String street) {
    this.street.set(street);
  }

  public StringProperty streetProperty() {
    return street;
  }

  public String getNumber() {
    return number.get();
  }

  public void setNumber(String number) {
    this.number.set(number);
  }

  public StringProperty numberProperty() {
    return number;
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

    var that = (Address) obj;
    return Objects.equals(region.get(), that.region.get())
           && Objects.equals(town.get(), that.town.get())
           && Objects.equals(street.get(), that.street.get())
           && Objects.equals(number.get(), that.number.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), region.get(), town.get(), street.get(), number.get());
  }

  public static class Builder {

    private long id = -1L;
    private String region;
    private String town;
    private String street;
    private String number;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setRegion(String region) {
      this.region = region;
      return this;
    }

    public Builder setTown(String town) {
      this.town = town;
      return this;
    }

    public Builder setStreet(String street) {
      this.street = street;
      return this;
    }

    public Builder setNumber(String number) {
      this.number = number;
      return this;
    }

    public Address build() {
      if (number != null) {
        Objects.requireNonNull(street);
      }
      var instance = new Address();
      instance.setId(id);
      instance.setRegion(Objects.requireNonNull(region));
      instance.setTown(Objects.requireNonNull(town));
      instance.setStreet(street);
      instance.setNumber(number);
      return instance;
    }
  }
}
