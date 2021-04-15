package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.controller.Home;
import com.github.ecstasyawesome.warehouse.controller.ProductList;
import com.github.ecstasyawesome.warehouse.util.ResourceLoader;
import java.net.URL;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;

public enum Controller {

  MAIN(Access.USER, Home.FXML),
  PRODUCT_LIST(Access.USER, ProductList.FXML);

  public final Parent parent;
  public final Access access;

  Controller(Access access, URL resource) {
    this.access = access;
    this.parent = ResourceLoader.load(resource);
  }

  public Scene getScene() {
    return Objects.requireNonNullElseGet(parent.getScene(), () -> new Scene(parent));
  }
}
