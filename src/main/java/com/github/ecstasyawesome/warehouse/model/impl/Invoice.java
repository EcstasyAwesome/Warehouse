package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractReceiveOperation;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Invoice extends AbstractReceiveOperation {

  private final ObjectProperty<ProductProvider> productProvider = new SimpleObjectProperty<>();

  public Invoice(Invoice instance) {
    setId(instance.getId());
    setProductProvider(new ProductProvider(instance.getProductProvider()));
    setProductStorage(new ProductStorage(instance.getProductStorage()));
    setTime(LocalDateTime.of(instance.getTime().toLocalDate(), instance.getTime().toLocalTime()));
    setUser(new User(instance.getUser()));
  }

  private Invoice() {
  }

  public ProductProvider getProductProvider() {
    return productProvider.get();
  }

  public void setProductProvider(ProductProvider productProvider) {
    this.productProvider.set(productProvider);
  }

  public ObjectProperty<ProductProvider> productProviderProperty() {
    return productProvider;
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

    var that = (Invoice) obj;
    return Objects.equals(productProvider.get(), that.productProvider.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), productProvider.get());
  }

  public static class Builder {

    private long id = -1L;
    private ProductProvider productProvider;
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

    public Builder setProductProvider(ProductProvider productProvider) {
      this.productProvider = productProvider;
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

    public Invoice build() {
      var instance = new Invoice();
      instance.setId(id);
      instance.setProductProvider(Objects.requireNonNull(productProvider));
      instance.setProductStorage(Objects.requireNonNull(productStorage));
      instance.setTime(Objects.requireNonNull(time));
      instance.setUser(user);
      return instance;
    }
  }
}
