package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractReceiveOperationItem;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class InvoiceItem extends AbstractReceiveOperationItem {

  private final DoubleProperty purchasePrice = new SimpleDoubleProperty();

  public InvoiceItem(InvoiceItem instance) {
    setId(instance.getId());
    setProduct(new Product(instance.getProduct()));
    setAmount(instance.getAmount());
    setPurchasePrice(instance.getPurchasePrice());
  }

  private InvoiceItem() {
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

    var that = (InvoiceItem) obj;
    return purchasePrice.get() == that.purchasePrice.get();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), purchasePrice.get());
  }

  public static class Builder {

    private long id = -1L;
    private Product product;
    private double amount = 0D;
    private double purchasePrice = 0D;

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

    public Builder setPurchasePrice(double purchasePrice) {
      this.purchasePrice = purchasePrice;
      return this;
    }

    public InvoiceItem build() {
      if (amount <= 0D || purchasePrice <= 0D) {
        throw new NullPointerException("Amount or price cannot be negative or zero");
      }
      var instance = new InvoiceItem();
      instance.setId(id);
      instance.setProduct(Objects.requireNonNull(product));
      instance.setAmount(amount);
      instance.setPurchasePrice(purchasePrice);
      return instance;
    }
  }
}
