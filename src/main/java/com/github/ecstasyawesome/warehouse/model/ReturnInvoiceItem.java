package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ReturnInvoiceItem extends AbstractReceiveOperationItem {

  private final ObjectProperty<ReturnInvoice> returnInvoice = new SimpleObjectProperty<>();
  private final DoubleProperty price = new SimpleDoubleProperty();

  public ReturnInvoiceItem(ReturnInvoiceItem instance) {
    setId(instance.getId());
    setProduct(new Product(instance.getProduct()));
    setAmount(instance.getAmount());
    setReturnInvoice(new ReturnInvoice(instance.getReturnInvoice()));
    setPrice(instance.getPrice());
  }

  private ReturnInvoiceItem() {
  }

  public ReturnInvoice getReturnInvoice() {
    return returnInvoice.get();
  }

  public void setReturnInvoice(ReturnInvoice returnInvoice) {
    this.returnInvoice.set(returnInvoice);
  }

  public ObjectProperty<ReturnInvoice> returnInvoiceProperty() {
    return returnInvoice;
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

    var that = (ReturnInvoiceItem) obj;
    return Objects.equals(returnInvoice.get(), that.returnInvoice.get())
           && Objects.equals(price.get(), that.price.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), returnInvoice.get(), price.get());
  }

  public static class Builder {

    private long id = -1L;
    private Product product;
    private double amount = 0D;
    private ReturnInvoice returnInvoice;
    private double price = 0D;

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

    public Builder setReturnInvoice(ReturnInvoice invoice) {
      this.returnInvoice = invoice;
      return this;
    }

    public Builder setPrice(double price) {
      this.price = price;
      return this;
    }

    public ReturnInvoiceItem build() {
      if (amount <= 0D || price <= 0D) {
        throw new NullPointerException("Amount or price cannot be negative or zero");
      }
      var instance = new ReturnInvoiceItem();
      instance.setId(id);
      instance.setProduct(Objects.requireNonNull(product));
      instance.setAmount(amount);
      instance.setReturnInvoice(Objects.requireNonNull(returnInvoice));
      instance.setPrice(price);
      return instance;
    }
  }
}
