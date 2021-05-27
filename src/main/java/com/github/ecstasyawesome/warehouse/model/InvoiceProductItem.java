package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class InvoiceProductItem extends AbstractProductItem {

  private final ObjectProperty<Invoice> invoice = new SimpleObjectProperty<>();
  private final DoubleProperty purchasePrice = new SimpleDoubleProperty();

  public InvoiceProductItem(InvoiceProductItem instance) {
    setId(instance.getId());
    setProduct(new Product(instance.getProduct()));
    setAmount(instance.getAmount());
    setInvoice(new Invoice(instance.getInvoice()));
    setPurchasePrice(instance.getPurchasePrice());
  }

  private InvoiceProductItem() {
  }

  public Invoice getInvoice() {
    return invoice.get();
  }

  public void setInvoice(Invoice invoice) {
    this.invoice.set(invoice);
  }

  public ObjectProperty<Invoice> invoiceProperty() {
    return invoice;
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

    var that = (InvoiceProductItem) obj;
    return Objects.equals(invoice.get(), that.invoice.get())
           && purchasePrice.get() == that.purchasePrice.get();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), invoice.get(), purchasePrice.get());
  }

  public static class Builder {

    private long id = -1L;
    private Product product;
    private double amount = 0D;
    private Invoice invoice;
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

    public Builder setInvoice(Invoice invoice) {
      this.invoice = invoice;
      return this;
    }

    public Builder setPurchasePrice(double purchasePrice) {
      this.purchasePrice = purchasePrice;
      return this;
    }

    public InvoiceProductItem build() {
      if (amount <= 0D || purchasePrice <= 0D) {
        throw new NullPointerException("Amount or price cannot be negative or zero");
      }
      var instance = new InvoiceProductItem();
      instance.setId(id);
      instance.setProduct(Objects.requireNonNull(product));
      instance.setAmount(amount);
      instance.setInvoice(Objects.requireNonNull(invoice));
      instance.setPurchasePrice(purchasePrice);
      return instance;
    }
  }
}
