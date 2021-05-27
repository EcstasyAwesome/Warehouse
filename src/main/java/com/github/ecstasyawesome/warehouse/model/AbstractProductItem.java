package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class AbstractProductItem extends AbstractRecord {

  private final ObjectProperty<Product> product = new SimpleObjectProperty<>();
  private final DoubleProperty amount = new SimpleDoubleProperty();

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
    if (!super.equals(obj)) {
      return false;
    }

    var that = (AbstractProductItem) obj;
    return Objects.equals(product.get(), that.product.get()) && amount.get() == that.amount.get();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), product.get(), amount.get());
  }
}
