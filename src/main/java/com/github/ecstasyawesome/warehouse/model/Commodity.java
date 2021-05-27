package com.github.ecstasyawesome.warehouse.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Commodity extends AbstractCommodity {

  public Commodity(Commodity instance) {
    setId(instance.getId());
    setProduct(new Product(instance.getProduct()));
    setAmount(instance.getAmount());
    setProductStorage(new ProductStorage(instance.getProductStorage()));
    setPurchasePrice(instance.getPurchasePrice());
    setRetailPrice(instance.getRetailPrice());
    var time = instance.getUpdateTime();
    setUpdateTime(LocalDateTime.of(time.toLocalDate(), time.toLocalTime()));
  }

  private Commodity() {
  }

  public static class Builder {

    private long id = -1L;
    private Product product;
    private double amount = 0D;
    private ProductStorage productStorage;
    private double retailPrice = 0D;
    private double purchasePrice = 0D;
    private LocalDateTime updateTime;

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

    public Commodity build() {
      if (amount <= 0D || purchasePrice <= 0D || retailPrice <= 0D) {
        throw new NullPointerException("Amount or price cannot be negative or zero");
      }
      var instance = new Commodity();
      instance.setId(id);
      instance.setProduct(Objects.requireNonNull(product));
      instance.setAmount(amount);
      instance.setProductStorage(Objects.requireNonNull(productStorage));
      instance.setPurchasePrice(purchasePrice);
      instance.setRetailPrice(retailPrice);
      instance.setUpdateTime(Objects.requireNonNull(updateTime));
      return instance;
    }
  }
}
