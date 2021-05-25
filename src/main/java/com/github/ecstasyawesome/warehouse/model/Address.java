package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address extends Record {

  private final StringProperty region;
  private final StringProperty town;
  private final StringProperty street;
  private final StringProperty number;

  public Address(Address instance) {
    this(instance.getId(), instance.getRegion(), instance.getTown(), instance.getStreet(),
        instance.getNumber());
  }

  private Address(long id, String region, String town, String street, String number) {
    super(id);
    this.region = new SimpleStringProperty(region);
    this.town = new SimpleStringProperty(town);
    this.street = new SimpleStringProperty(street);
    this.number = new SimpleStringProperty(number);
  }

  public static Builder getBuilder() {
    return new Builder();
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
      return new Address(id,
          Objects.requireNonNull(region),
          Objects.requireNonNull(town),
          street,
          number);
    }
  }
}
