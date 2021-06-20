package com.github.ecstasyawesome.warehouse.model.impl;

import com.github.ecstasyawesome.warehouse.model.AbstractReceiveOperation;
import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order extends AbstractReceiveOperation {

  private final ObjectProperty<ProductProvider> productProvider = new SimpleObjectProperty<>();
  private final StringProperty comment = new SimpleStringProperty();

  public Order(Order instance) {
    setId(instance.getId());
    setProductProvider(new ProductProvider(instance.getProductProvider()));
    setProductStorage(new ProductStorage(instance.getProductStorage()));
    setTime(LocalDateTime.of(instance.getTime().toLocalDate(), instance.getTime().toLocalTime()));
    setUser(new User(instance.getUser()));
    setComment(instance.getComment());
  }

  private Order() {
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

  public String getComment() {
    return comment.get();
  }

  public void setComment(String comment) {
    this.comment.set(comment);
  }

  public StringProperty commentProperty() {
    return comment;
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

    var that = (Order) obj;
    return Objects.equals(getProductProvider(), that.getProductProvider())
           && Objects.equals(getComment(), that.getComment());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), productProvider.get(), comment.get());
  }

  public static class Builder {

    private long id = -1L;
    private ProductProvider productProvider;
    private ProductStorage productStorage;
    private LocalDateTime time;
    private User user;
    private String comment;

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

    public Builder setUser(User user) {
      this.user = user;
      return this;
    }

    public Builder setComment(String comment) {
      this.comment = comment;
      return this;
    }

    public Order build() {
      var instance = new Order();
      instance.setId(id);
      instance.setProductProvider(Objects.requireNonNull(productProvider));
      instance.setProductStorage(Objects.requireNonNull(productStorage));
      instance.setTime(Objects.requireNonNull(time));
      instance.setUser(user);
      instance.setComment(comment);
      return instance;
    }
  }
}
