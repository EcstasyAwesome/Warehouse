package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.util.PropertyTool.Config;
import com.github.ecstasyawesome.warehouse.util.ResourceLoader.Language;

public final class ApplicationSettings extends Settings {

  private static ApplicationSettings instance;
  private final String languageDefaultValue = Language.ENGLISH.name();
  private final String languageKey = "application.language";

  private ApplicationSettings() {
    super(Config.APPLICATION);
  }

  public static ApplicationSettings getInstance() {
    if (instance == null) {
      instance = new ApplicationSettings();
    }
    return instance;
  }

  public Language getLanguage() {
    var value = properties.getProperty(languageKey, languageDefaultValue);
    try {
      return Language.valueOf(value);
    } catch (IllegalArgumentException exception) {
      return Language.valueOf(languageDefaultValue);
    }
  }

  public void setLanguage(Language language) {
    properties.setProperty(languageKey, language.name());
  }
}
