package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.util.ResourceLoader;
import java.io.IOException;
import java.net.URL;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class ModuleWrapper<T extends Controller> {

  private final Access access;
  private final Parent parent;
  private final Scene scene;
  private final T controller;

  public ModuleWrapper(Access access, final URL url) {
    this.access = access;
    var fxmlLoader = ResourceLoader.createFxmlLoader(url);
    try {
      parent = fxmlLoader.load();
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
    scene = new Scene(parent);
  }

  public Access getAccess() {
    return access;
  }

  @SuppressWarnings("unchecked")
  public <E extends Parent> E getParent() {
    return (E) parent;
  }

  public Scene getScene() {
    return scene;
  }

  public T getController() {
    return controller;
  }
}
