package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractCommodity;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DecommissionedCommodity extends AbstractCommodity {

  private final ObjectProperty<LocalDateTime> decommissioningTime = new SimpleObjectProperty<>();
  private final StringProperty reason = new SimpleStringProperty();
  private final ObjectProperty<User> user = new SimpleObjectProperty<>();

  public DecommissionedCommodity(DecommissionedCommodity instance) {
    setId(instance.getId());
    setProduct(new Product(instance.getProduct()));
    setAmount(instance.getAmount());
    setProductStorage(new ProductStorage(instance.getProductStorage()));
    setPurchasePrice(instance.getPurchasePrice());
    setRetailPrice(instance.getRetailPrice());
    var timeOne = instance.getUpdateTime();
    setUpdateTime(LocalDateTime.of(timeOne.toLocalDate(), timeOne.toLocalTime()));
    var timeTwo = instance.getDecommissioningTime();
    setDecommissioningTime(LocalDateTime.of(timeTwo.toLocalDate(), timeTwo.toLocalTime()));
    setReason(instance.getReason());
    setUser(new User(instance.getUser()));
  }

  private DecommissionedCommodity() {
  }

  public LocalDateTime getDecommissioningTime() {
    return decommissioningTime.get();
  }

  public void setDecommissioningTime(LocalDateTime decommissioningTime) {
    this.decommissioningTime.set(decommissioningTime);
  }

  public ObjectProperty<LocalDateTime> decommissioningTimeProperty() {
    return decommissioningTime;
  }

  public String getReason() {
    return reason.get();
  }

  public void setReason(String reason) {
    this.reason.set(reason);
  }

  public StringProperty reasonProperty() {
    return reason;
  }

  public User getUser() {
    return user.get();
  }

  public void setUser(User user) {
    this.user.set(user);
  }

  public ObjectProperty<User> userProperty() {
    return user;
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

    var that = (DecommissionedCommodity) obj;
    return Objects.equals(decommissioningTime.get(), that.decommissioningTime.get())
           && Objects.equals(reason.get(), that.reason.get())
           && Objects.equals(user.get(), that.user.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), decommissioningTime.get(), reason.get(), user.get());
  }

  public static class Builder {

    private long id = -1L;
    private Product product;
    private double amount = 0D;
    private ProductStorage productStorage;
    private double retailPrice = 0D;
    private double purchasePrice = 0D;
    private LocalDateTime updateTime;
    private LocalDateTime decommissioningTime;
    private String reason;
    private User user;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setProduct(Product product) {
      this.product = product;
      return this;
    }

    public Builder setAmount(double amount) {
      this.amount = amount;
      return this;
    }

    public Builder setProductStorage(ProductStorage productStorage) {
      this.productStorage = productStorage;
      return this;
    }

    public Builder setRetailPrice(double retailPrice) {
      this.retailPrice = retailPrice;
      return this;
    }

    public Builder setPurchasePrice(double purchasePrice) {
      this.purchasePrice = purchasePrice;
      return this;
    }

    public Builder setUpdateTime(LocalDateTime updateTime) {
      this.updateTime = updateTime;
      return this;
    }

    public Builder setDecommissioningTime(LocalDateTime decommissioningTime) {
      this.decommissioningTime = decommissioningTime;
      return this;
    }

    public Builder setReason(String reason) {
      this.reason = reason;
      return this;
    }

    public Builder setUser(User user) {
      this.user = user;
      return this;
    }

    public DecommissionedCommodity build() {
      if (amount <= 0D || purchasePrice <= 0D || retailPrice <= 0D) {
        throw new NullPointerException("Amount or price cannot be negative or zero");
      }
      var instance = new DecommissionedCommodity();
      instance.setId(id);
      instance.setProduct(Objects.requireNonNull(product));
      instance.setAmount(amount);
      instance.setProductStorage(Objects.requireNonNull(productStorage));
      instance.setRetailPrice(retailPrice);
      instance.setPurchasePrice(purchasePrice);
      instance.setUpdateTime(Objects.requireNonNull(updateTime));
      instance.setDecommissioningTime(Objects.requireNonNull(decommissioningTime));
      instance.setReason(Objects.requireNonNull(reason));
      instance.setUser(Objects.requireNonNull(user));
      return instance;
    }
  }
}
