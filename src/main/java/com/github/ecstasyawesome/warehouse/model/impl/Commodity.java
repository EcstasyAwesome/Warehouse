package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.ProductItem;
import com.github.ecstasyawesome.warehouse.model.Record;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Commodity extends Record {

  private final ObjectProperty<ProductItem> productItem;
  private final ObjectProperty<ProductStorage> productStorage;
  private final DoubleProperty purchasePrice;
  private final DoubleProperty retailPrice;
  private final ObjectProperty<LocalDateTime> updateTime;

  public Commodity(Commodity instance) {
    this(instance.getId(),
        new ProductItem(instance.getProductItem()),
        new ProductStorage(instance.getProductStorage()), instance.getPurchasePrice(),
        instance.getRetailPrice(),
        LocalDateTime
            .of(instance.getUpdateTime().toLocalDate(), instance.getUpdateTime().toLocalTime()));
  }

  private Commodity(long id, ProductItem productItem, ProductStorage productStorage,
      double purchasePrice, double retailPrice, LocalDateTime updateTime) {
    super(id);
    this.productItem = new SimpleObjectProperty<>(productItem);
    this.productStorage = new SimpleObjectProperty<>(productStorage);
    this.purchasePrice = new SimpleDoubleProperty(purchasePrice);
    this.retailPrice = new SimpleDoubleProperty(retailPrice);
    this.updateTime = new SimpleObjectProperty<>(updateTime);
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public ProductItem getProductItem() {
    return productItem.get();
  }

  public void setProductItem(ProductItem productItem) {
    this.productItem.set(productItem);
  }

  public ObjectProperty<ProductItem> productItemProperty() {
    return productItem;
  }

  public ProductStorage getProductStorage() {
    return productStorage.get();
  }

  public void setProductStorage(ProductStorage productStorage) {
    this.productStorage.set(productStorage);
  }

  public ObjectProperty<ProductStorage> productStorageProperty() {
    return productStorage;
  }

  public double getPurchasePrice() {
    return purchasePrice.get();
  }

  public void setPurchasePrice(double purchasePrice) {
    this.purchasePrice.set(purchasePrice);
  }

  public DoubleProperty purchasePriceProperty() {
    return purchasePrice;
  }

  public double getRetailPrice() {
    return retailPrice.get();
  }

  public void setRetailPrice(double retailPrice) {
    this.retailPrice.set(retailPrice);
  }

  public DoubleProperty retailPriceProperty() {
    return retailPrice;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime.get();
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime.set(updateTime);
  }

  public ObjectProperty<LocalDateTime> updateTimeProperty() {
    return updateTime;
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

    var that = (Commodity) obj;
    return Objects.equals(productItem.get(), that.productItem.get())
           && Objects.equals(productStorage.get(), that.productStorage.get())
           && purchasePrice.get() == that.purchasePrice.get()
           && retailPrice.get() == that.retailPrice.get()
           && Objects.equals(updateTime.get(), that.updateTime.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), productItem.get(), productStorage.get(),
        purchasePrice.get(), retailPrice.get(), updateTime.get());
  }

  public static class Builder {

    private long id = -1L;
    private ProductItem productItem;
    private ProductStorage productStorage;
    private double purchasePrice = -1D;
    private double retailPrice = -1D;
    private LocalDateTime updateTime;

    private Builder() {
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setProductItem(ProductItem productItem) {
      this.productItem = productItem;
      return this;
    }

    public Builder setProductStorage(ProductStorage productStorage) {
      this.productStorage = productStorage;
      return this;
    }

    public Builder setPurchasePrice(double purchasePrice) {
      this.purchasePrice = purchasePrice;
      return this;
    }

    public Builder setRetailPrice(double retailPrice) {
      this.retailPrice = retailPrice;
      return this;
    }

    public Builder setUpdateTime(LocalDateTime updateTime) {
      this.updateTime = updateTime;
      return this;
    }

    public Commodity build() {
      if (purchasePrice <= 0D || retailPrice <= 0D) {
        throw new NullPointerException("Price cannot be negative or zero");
      }
      return new Commodity(id,
          Objects.requireNonNull(productItem),
          Objects.requireNonNull(productStorage),
          purchasePrice,
          retailPrice,
          Objects.requireNonNull(updateTime));
    }
  }
}
