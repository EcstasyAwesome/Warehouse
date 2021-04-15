package com.github.ecstasyawesome.warehouse.util;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public final class ResourceLoader {

  public static final ResourceBundle LANGUAGE_BUNDLE = loadResourceBundle();

  private ResourceLoader() {
  }

  public static <T extends Parent> T load(URL resource) {
    Objects.requireNonNull(resource);
    try {
      return FXMLLoader.load(resource, LANGUAGE_BUNDLE);
    } catch (Exception exception) {
      throw new ExceptionInInitializerError(exception);
    }
  }

  private static ResourceBundle loadResourceBundle() {
    // TODO preload locale from app settings
    return ResourceBundle.getBundle("language/locale");
  }
}
