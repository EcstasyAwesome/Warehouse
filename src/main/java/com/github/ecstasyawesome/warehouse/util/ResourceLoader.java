package com.github.ecstasyawesome.warehouse.util;

import com.github.ecstasyawesome.warehouse.core.ApplicationSettings;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ResourceLoader {

  private static final Logger LOGGER = LogManager.getLogger(ResourceLoader.class);
  private static final ResourceBundle LANGUAGE_BUNDLE = loadResourceBundle();

  private ResourceLoader() {
  }

  public static <T extends Parent> T load(URL resource) {
    Objects.requireNonNull(resource);
    try {
      T parent = FXMLLoader.load(resource, LANGUAGE_BUNDLE);
      LOGGER.debug("A parent loaded successfully from the resource '{}'", resource);
      return parent;
    } catch (IOException exception) {
      throw new IllegalArgumentException(LOGGER.throwing(Level.FATAL, exception));
    }
  }

  public static FXMLLoader createFxmlLoader(URL resource) {
    Objects.requireNonNull(resource);
    return new FXMLLoader(resource, LANGUAGE_BUNDLE);
  }

  private static ResourceBundle loadResourceBundle() {
    var language = ApplicationSettings.getInstance().getLanguage();
    try {
      var bundle = ResourceBundle.getBundle("language/locale", language.locale);
      LOGGER.debug("Resource bundle loaded. Language is '{}'", language);
      return bundle;
    } catch (MissingResourceException exception) {
      throw LOGGER.throwing(Level.FATAL, exception);
    }
  }

  public enum Language {

    ENGLISH(Locale.ROOT, "English"),
    RUSSIAN(new Locale("ru", "RU"), "Русский"),
    UKRAINIAN(new Locale("uk", "UA"), "Українська");

    public final String originalName;
    private final Locale locale;

    Language(Locale locale, String originalName) {
      this.locale = locale;
      this.originalName = originalName;
    }

    public static ObservableList<Language> getLanguages() {
      return FXCollections.observableArrayList(Language.values());
    }

    @Override
    public String toString() {
      return originalName;
    }
  }
}
