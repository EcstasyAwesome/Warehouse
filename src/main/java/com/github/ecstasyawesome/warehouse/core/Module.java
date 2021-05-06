package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.util.ResourceLoader;
import java.io.IOException;
import java.net.URL;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public abstract class Module<T extends Controller> {

  private final Parent parent;
  private final Scene scene;
  private final T controller;

  public Module(final URL url) {
    var logger = LogManager.getLogger(getClass());
    var fxmlLoader = ResourceLoader.createFxmlLoader(url);
    try {
      parent = fxmlLoader.load();
      logger.debug("The resource '{}' loaded", url);
    } catch (IOException exception) {
      throw new IllegalArgumentException(logger.throwing(Level.FATAL, exception));
    }
    try {
      controller = fxmlLoader.getController();
      logger.debug("The controller '{}' created", controller.getClass().getName());
    } catch (ClassCastException exception) {
      throw logger.throwing(Level.FATAL, exception);
    }
    scene = new Scene(parent);
    logger.debug("The module initialized successfully");
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
