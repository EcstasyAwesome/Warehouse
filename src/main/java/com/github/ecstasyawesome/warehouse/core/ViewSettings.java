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
  private final String maximizedKey = "window.maximized";

  private ViewSettings() {
  }

  public static ViewSettings getInstance() {
    return INSTANCE;
  }

  public double getWidth() {
    return getVerifiedWithDefaultLengthValue(widthKey, widthDefaultValue);
  }

  public double getDefaultWidth() {
    return Double.parseDouble(widthDefaultValue);
  }

  public void setWidth(double width) {
    properties.setProperty(widthKey, String.valueOf(width));
  }

  public double getHeight() {
    return getVerifiedWithDefaultLengthValue(heightKey, heightDefaultValue);
  }

  public double getDefaultHeight() {
    return Double.parseDouble(heightDefaultValue);
  }

  private double getVerifiedWithDefaultLengthValue(String key, String defaultValue) {
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

  public boolean isMaximized() {
    return Boolean.parseBoolean(properties.getProperty(maximizedKey));
  }

  public void setMaximized(boolean maximized) {
    properties.setProperty(maximizedKey, String.valueOf(maximized));
  }

  public void save() {
    PropertyTool.save(Config.VIEW, properties);
  }
}
