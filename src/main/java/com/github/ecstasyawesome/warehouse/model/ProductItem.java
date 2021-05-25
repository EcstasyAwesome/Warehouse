package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProductItem {

  private final ObjectProperty<Product> product;
  private final DoubleProperty amount;

  public ProductItem(ProductItem instance) {
    this(instance.getProduct(), instance.getAmount());
  }

  private ProductItem(Product product, double amount) {
    if (amount <= 0D) {
      throw new NullPointerException("Amount cannot be negative or zero");
    }
    this.product = new SimpleObjectProperty<>(product);
    this.amount = new SimpleDoubleProperty(amount);
  }

  public Product getProduct() {
    return product.get();
  }

  public void setProduct(Product product) {
    this.product.set(product);
  }

  public ObjectProperty<Product> productProperty() {
    return product;
  }

  public double getAmount() {
    return amount.get();
  }

  public void setAmount(double amount) {
    this.amount.set(amount);
  }

  public DoubleProperty amountProperty() {
    return amount;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    var that = (ProductItem) obj;
    return Objects.equals(product.get(), that.product.get()) && amount.get() == that.amount.get();
  }

  @Override
  public int hashCode() {
    return Objects.hash(product.get(), amount.get());
  }
}
