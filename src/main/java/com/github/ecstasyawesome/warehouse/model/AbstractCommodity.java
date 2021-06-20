package com.github.ecstasyawesome.warehouse.model;

import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class AbstractCommodity extends AbstractReceiveOperationItem {

  private final ObjectProperty<ProductStorage> productStorage = new SimpleObjectProperty<>();
  private final DoubleProperty purchasePrice = new SimpleDoubleProperty();
  private final DoubleProperty retailPrice = new SimpleDoubleProperty();
  private final ObjectProperty<LocalDateTime> updateTime = new SimpleObjectProperty<>();

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

    var that = (AbstractCommodity) obj;
    return Objects.equals(getProductStorage(), that.getProductStorage())
           && getPurchasePrice() == that.getPurchasePrice()
           && getRetailPrice() == that.getRetailPrice()
           && Objects.equals(getUpdateTime(), that.getUpdateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getProductStorage(), getPurchasePrice(), getRetailPrice(),
        getUpdateTime());
  }
}
