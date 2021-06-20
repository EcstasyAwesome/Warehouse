package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractContact;
import com.github.ecstasyawesome.warehouse.model.Recoverable;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BusinessContact extends AbstractContact implements Recoverable<BusinessContact> {

  private final StringProperty extraPhone = new SimpleStringProperty();
  private final StringProperty site = new SimpleStringProperty();

  public BusinessContact(BusinessContact instance) {
    setId(instance.getId());
    setPhone(instance.getPhone());
    setEmail(instance.getEmail());
    setExtraPhone(instance.getExtraPhone());
    setSite(instance.getSite());
  }

  private BusinessContact() {
  }

  public String getExtraPhone() {
    return extraPhone.get();
  }

  public void setExtraPhone(String extraPhone) {
    this.extraPhone.set(extraPhone);
  }

  public StringProperty extraPhoneProperty() {
    return extraPhone;
  }

  public String getSite() {
    return site.get();
  }

  public void setSite(String site) {
    this.site.set(site);
  }

  public StringProperty siteProperty() {
    return site;
  }

  @Override
  public void recover(BusinessContact instance) {
    setId(instance.getId());
    setPhone(instance.getPhone());
    setEmail(instance.getEmail());
    setExtraPhone(instance.getExtraPhone());
    setSite(instance.getSite());
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

    var that = (BusinessContact) obj;
    return Objects.equals(getExtraPhone(), that.getExtraPhone())
           && Objects.equals(getSite(), that.getSite());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getExtraPhone(), getSite());
  }

  public static class Builder {

    private long id = -1L;
    private String phone;
    private String extraPhone;
    private String email;
    private String site;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setPhone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder setExtraPhone(String extraPhone) {
      this.extraPhone = extraPhone;
      return this;
    }

    public Builder setEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder setSite(String site) {
      this.site = site;
      return this;
    }

    public BusinessContact build() {
      var instance = new BusinessContact();
      instance.setId(id);
      instance.setPhone(Objects.requireNonNull(phone));
      instance.setEmail(email);
      instance.setExtraPhone(extraPhone);
      instance.setSite(site);
      return instance;
    }
  }
}
