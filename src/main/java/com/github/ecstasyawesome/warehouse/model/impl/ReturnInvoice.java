package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractReceiveOperation;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ReturnInvoice extends AbstractReceiveOperation {

  private final ObjectProperty<Customer> customer = new SimpleObjectProperty<>();
  // TODO receipt

  public ReturnInvoice(ReturnInvoice instance) {
    setId(instance.getId());
    setCustomer(new Customer(instance.getCustomer()));
    setProductStorage(new ProductStorage(instance.getProductStorage()));
    setTime(LocalDateTime.of(instance.getTime().toLocalDate(), instance.getTime().toLocalTime()));
    setUser(new User(instance.getUser()));
  }

  private ReturnInvoice() {
  }

  public Customer getCustomer() {
    return customer.get();
  }

  public void setCustomer(Customer customer) {
    this.customer.set(customer);
  }

  public ObjectProperty<Customer> customerProperty() {
    return customer;
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

    var that = (ReturnInvoice) obj;
    return Objects.equals(customer.get(), that.customer.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), customer.get());
  }

  public static class Builder {

    private long id = -1L;
    private Customer customer;
    private ProductStorage productStorage;
    private LocalDateTime time;
    private User user;

    private Builder() {
    }

    public static Builder create() {
      return new Builder();
    }

    public Builder setId(long id) {
      this.id = id;
      return this;
    }

    public Builder setCustomer(Customer customer) {
      this.customer = customer;
      return this;
    }

    public Builder setProductStorage(ProductStorage productStorage) {
      this.productStorage = productStorage;
      return this;
    }

    public Builder setTime(LocalDateTime time) {
      this.time = time;
      return this;
    }

    private Builder setUser(User user) {
      this.user = user;
      return this;
    }

    public ReturnInvoice build() {
      var instance = new ReturnInvoice();
      instance.setId(id);
      instance.setCustomer(Objects.requireNonNull(customer));
      instance.setProductStorage(Objects.requireNonNull(productStorage));
      instance.setTime(Objects.requireNonNull(time));
      instance.setUser(user);
      return instance;
    }
  }
}
