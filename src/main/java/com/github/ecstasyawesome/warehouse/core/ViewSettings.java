package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.util.PropertyTool;
import com.github.ecstasyawesome.warehouse.util.PropertyTool.Config;
import java.util.Properties;

public final class ViewSettings {

  private static final ViewSettings INSTANCE = new ViewSettings();
  private final Properties properties = PropertyTool.load(Config.VIEW);
  private final String widthKey = "window.width";
  private final String widthDefaultValue = "640.0";
  private final String heightKey = "window.height";
  private final String heightDefaultValue = "480.0";
  // TODO fullScreen setting

  private ViewSettings() {
  }

  public static ViewSettings getInstance() {
    return INSTANCE;
  }

  public double getWidth() {
    return getVerifiedWithDefaultValue(widthKey, widthDefaultValue);
  }

  public double getDefaultWidth() {
    return Double.parseDouble(widthDefaultValue);
  }

  public void setWidth(double width) {
    properties.setProperty(widthKey, String.valueOf(width));
  }

  public double getHeight() {
    return getVerifiedWithDefaultValue(heightKey, heightDefaultValue);
  }

  public double getDefaultHeight() {
    return Double.parseDouble(heightDefaultValue);
  }

  private double getVerifiedWithDefaultValue(String key, String defaultValue) {
    var value = properties.getProperty(key, defaultValue);
    var result = Double.parseDouble(value);
    var defaultDoubleValue = Double.parseDouble(defaultValue);
    if (result < defaultDoubleValue) {
      properties.setProperty(key, defaultValue);
      return defaultDoubleValue;
    }
    return result;
  }

  public void setHeight(double height) {
    properties.setProperty(heightKey, String.valueOf(height));
  }

  public void save() {
    PropertyTool.save(Config.VIEW, properties);
  }
}
