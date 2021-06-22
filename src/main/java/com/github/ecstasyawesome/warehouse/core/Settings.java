package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.util.PropertyTool;
import com.github.ecstasyawesome.warehouse.util.PropertyTool.Config;
import java.util.Properties;

public abstract class Settings {

  protected final Properties properties;
  private final Config config;

  protected Settings(Config config) {
    this.config = config;
    properties = PropertyTool.load(config);
  }

  public final void save() {
    PropertyTool.save(config, properties);
  }
}
