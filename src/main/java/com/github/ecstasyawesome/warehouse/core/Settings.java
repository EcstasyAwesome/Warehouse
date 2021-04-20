package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.util.PropertyTool;
import com.github.ecstasyawesome.warehouse.util.PropertyTool.Config;
import java.util.Properties;

public abstract class Settings {

  private final Config config;
  protected final Properties properties;

  protected Settings(Config config) {
    this.config = config;
    properties = PropertyTool.load(config);
  }

  public final void save() {
    PropertyTool.save(config, properties);
  }
}
