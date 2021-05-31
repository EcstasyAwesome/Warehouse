package com.github.ecstasyawesome.warehouse.model;

import java.util.Objects;

public class OrderItem extends AbstractReceiveOperationItem {

  public OrderItem(OrderItem instance) {
    setId(instance.getId());
    setProduct(new Product(instance.getProduct()));
    setAmount(instance.getAmount());
  }

  private OrderItem() {
  }

  public static class Builder {

    private long id = -1L;
    private Product product;
    private double amount = 0D;

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

    public OrderItem build() {
      if (amount <= 0D) {
        throw new NullPointerException("Amount cannot be negative or zero");
      }
      var instance = new OrderItem();
      instance.setId(id);
      instance.setProduct(Objects.requireNonNull(product));
      instance.setAmount(amount);
      return instance;
    }
  }
}
