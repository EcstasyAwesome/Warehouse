package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.ProductItem;
import com.github.ecstasyawesome.warehouse.model.Record;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DecommissionedProduct extends Record {

  private final ObjectProperty<ProductItem> productItem;
  private final ObjectProperty<ProductStorage> productStorage;
  private final DoubleProperty price;
  private final ObjectProperty<LocalDateTime> time;

  public DecommissionedProduct(DecommissionedProduct instance) {
    this(instance.getId(),
        new ProductItem(instance.getProductItem()),
        new ProductStorage(instance.getProductStorage()),
        instance.getPrice(),
        LocalDateTime.of(instance.getTime().toLocalDate(), instance.getTime().toLocalTime()));
  }

  private DecommissionedProduct(long id, ProductItem productItem, ProductStorage productStorage,
      double price, LocalDateTime time) {
    super(id);
    this.productItem = new SimpleObjectProperty<>(productItem);
    this.productStorage = new SimpleObjectProperty<>(productStorage);
    this.price = new SimpleDoubleProperty(price);
    this.time = new SimpleObjectProperty<>(time);
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

  public double getPrice() {
    return price.get();
  }

  public void setPrice(double price) {
    this.price.set(price);
  }

  public DoubleProperty priceProperty() {
    return price;
  }

  public LocalDateTime getTime() {
    return time.get();
  }

  public void setTime(LocalDateTime time) {
    this.time.set(time);
  }

  public ObjectProperty<LocalDateTime> timeProperty() {
    return time;
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

    var that = (DecommissionedProduct) obj;
    return Objects.equals(productItem.get(), that.productItem.get())
           && Objects.equals(productStorage.get(), that.productStorage.get())
           && price.get() == that.price.get()
           && Objects.equals(time.get(), that.time.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), productItem.get(), productStorage.get(), price.get(),
        time.get());
  }

  public static class Builder {

    private long id = -1L;
    private ProductItem productItem;
    private ProductStorage productStorage;
    private double price = -1D;
    private LocalDateTime time;

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

    public Builder setPrice(double price) {
      this.price = price;
      return this;
    }

    public Builder setTime(LocalDateTime time) {
      this.time = time;
      return this;
    }

    public DecommissionedProduct build() {
      if (price <= 0D) {
        throw new NullPointerException("Price cannot be negative of zero");
      }
      return new DecommissionedProduct(id,
          Objects.requireNonNull(productItem),
          Objects.requireNonNull(productStorage),
          price,
          Objects.requireNonNull(time));
    }
  }
}
