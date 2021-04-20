package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.util.PropertyTool;
import com.github.ecstasyawesome.warehouse.util.PropertyTool.Config;
import com.github.ecstasyawesome.warehouse.util.ResourceLoader.Language;
import java.util.Properties;

public class ApplicationSettings extends Settings {

  private static ApplicationSettings instance;
  private final Properties properties = PropertyTool.load(Config.APPLICATION);

  private final String languageDefaultValue = Language.ENGLISH.name();
  private final String languageKey = "application.language";

  private ApplicationSettings() {
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

  @Override
  public void save() {
    PropertyTool.save(Config.APPLICATION, properties);
  }
}
