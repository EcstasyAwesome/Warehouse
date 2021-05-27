package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class OrderProductItem extends AbstractProductItem {

  private final ObjectProperty<Order> order = new SimpleObjectProperty<>();

  public OrderProductItem(OrderProductItem instance) {
    setId(instance.getId());
    setProduct(new Product(instance.getProduct()));
    setAmount(instance.getAmount());
    setOrder(new Order(instance.getOrder()));
  }

  private OrderProductItem() {
  }

  public Order getOrder() {
    return order.get();
  }

  public void setOrder(Order order) {
    this.order.set(order);
  }

  public ObjectProperty<Order> orderProperty() {
    return order;
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

    var that = (OrderProductItem) obj;
    return Objects.equals(order.get(), that.order.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), order.get());
  }

  public static class Builder {

    private long id = -1L;
    private Product product;
    private double amount = 0D;
    private Order order;

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

    public Builder setOrder(Order order) {
      this.order = order;
      return this;
    }

    public OrderProductItem build() {
      if (amount <= 0D) {
        throw new NullPointerException("Amount cannot be negative or zero");
      }
      var instance = new OrderProductItem();
      instance.setId(id);
      instance.setProduct(Objects.requireNonNull(product));
      instance.setAmount(amount);
      instance.setOrder(Objects.requireNonNull(order));
      return instance;
    }
  }
}
