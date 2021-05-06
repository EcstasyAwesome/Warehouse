package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.util.PropertyTool;
import com.github.ecstasyawesome.warehouse.util.PropertyTool.Config;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Settings {

  protected final Properties properties;
  protected final Logger logger = LogManager.getLogger(getClass());
  private final Config config;

  protected Settings(Config config) {
    this.config = config;
    properties = PropertyTool.load(config);
    logger.debug("Initialized");
  }

  public final void save() {
    logger.debug("Request to save settings");
    PropertyTool.save(config, properties);
  }
}
