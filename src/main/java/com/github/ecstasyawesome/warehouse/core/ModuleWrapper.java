package com.github.ecstasyawesome.warehouse.core;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public abstract class ModuleWrapper<T extends Controller> {

  private final Access access;
  private final Scene scene;
  private final T controller;

  public ModuleWrapper(Access access, final URL url) {
    this.access = access;
    var fxmlLoader = new FXMLLoader(url);
    try {
      scene = new Scene(fxmlLoader.load());
    } catch (IOException exception) {
      // TODO save a crash to some log
      throw new IllegalArgumentException(exception);
    }
    try {
      controller = fxmlLoader.getController();
    } catch (ClassCastException exception) {
      // TODO save a crash to some log
      throw new ClassCastException(exception.getMessage());
    }
  }

  public Access getAccess() {
    return access;
  }

  public Scene getScene() {
    return scene;
  }

  public T getController() {
    return controller;
  }
}
