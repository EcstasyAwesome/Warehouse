package com.github.ecstasyawesome.warehouse.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class AbstractReceiveOperation extends AbstractRecord {

  private final ObjectProperty<ProductStorage> productStorage = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDateTime> time = new SimpleObjectProperty<>();
  private final ObjectProperty<User> user = new SimpleObjectProperty<>();

  public ProductStorage getProductStorage() {
    return productStorage.get();
  }

  public void setProductStorage(ProductStorage productStorage) {
    this.productStorage.set(productStorage);
  }

  public ObjectProperty<ProductStorage> productStorageProperty() {
    return productStorage;
  }

  public LocalDateTime getTime() {
    return time.get();
  }

  public void setTime(LocalDateTime time) {
    this.time.set(time);
  }

  public ObjectProperty<LocalDateTime> timeProperty() {
    return time;
  }

  public User getUser() {
    return user.get();
  }

  public void setUser(User user) {
    this.user.set(user);
  }

  public ObjectProperty<User> userProperty() {
    return user;
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

    var that = (AbstractReceiveOperation) obj;
    return Objects.equals(productStorage.get(), that.productStorage.get())
           && Objects.equals(time.get(), that.time.get())
           && Objects.equals(user.get(), that.user.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), productStorage.get(), time.get(),
        user.get());
  }
}
