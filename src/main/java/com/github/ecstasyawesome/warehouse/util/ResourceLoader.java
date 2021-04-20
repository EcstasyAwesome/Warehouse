package com.github.ecstasyawesome.warehouse.util;

import com.github.ecstasyawesome.warehouse.core.ApplicationSettings;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public final class ResourceLoader {

  private static final ResourceBundle LANGUAGE_BUNDLE = loadResourceBundle();

  private ResourceLoader() {
  }

  public static <T extends Parent> T load(URL resource) {
    Objects.requireNonNull(resource);
    try {
      return FXMLLoader.load(resource, LANGUAGE_BUNDLE);
    } catch (IOException exception) {
      // TODO save to some logger
      throw new IllegalArgumentException(exception);
    }
  }

  public static FXMLLoader createFxmlLoader(URL resource) {
    Objects.requireNonNull(resource);
    return new FXMLLoader(resource, LANGUAGE_BUNDLE);
  }

  private static ResourceBundle loadResourceBundle() {
    var settings = ApplicationSettings.getInstance();
    return ResourceBundle.getBundle("language/locale", settings.getLanguage().locale);
  }

  public enum Language {

    ENGLISH(Locale.ROOT, "English"),
    RUSSIAN(new Locale("ru", "RU"), "Русский"),
    UKRAINIAN(new Locale("uk", "UA"), "Українська");

    private final Locale locale;
    public final String originalName;

    Language(Locale locale, String originalName) {
      this.locale = locale;
      this.originalName = originalName;
    }
  }
}
