package com.github.ecstasyawesome.warehouse.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PropertyTool {

  private static final Logger LOGGER = LogManager.getLogger(PropertyTool.class);
  private static final Path ROOT = Path.of("settings");

  private PropertyTool() {
  }

  public static Properties load(Config config) {
    Objects.requireNonNull(config, "Config is required");
    final var pathToFile = preparePath(config);
    final var result = new Properties();
    try (var inputStream = Files.newInputStream(pathToFile)) {
      result.load(inputStream);
      LOGGER.debug(String.format("%s loaded", config.fileName));
    } catch (IOException exception) {
      LOGGER.warn(String.format("%s is absent", config.fileName));
    }
    return result;
  }

  public static void save(Config config, final Properties properties) {
    Objects.requireNonNull(properties, "Properties is required");
    Objects.requireNonNull(config, "Config is required");
    verifyRootFolder();
    final var pathToFile = preparePath(config);
    try (var outputStream = Files.newOutputStream(pathToFile)) {
      properties.store(outputStream, "DO NOT MODIFY THIS FILE");
      LOGGER.debug(String.format("'%s' saved", config.fileName));
    } catch (IOException exception) {
      var message = String.format("Cannot save %s", config.fileName);
      LOGGER.error(message, exception);
    }
  }

  private static Path preparePath(Config config) {
    return ROOT.resolve(config.fileName);
  }

  private static void verifyRootFolder() {
    if (Files.notExists(ROOT)) {
      try {
        Files.createDirectory(ROOT);
        LOGGER.debug(String.format("Created the directory '%s'", ROOT.toAbsolutePath()));
      } catch (IOException exception) {
        var message = String.format("Cannot create the directory '%s'", ROOT.toAbsolutePath());
        LOGGER.error(message, exception);
      }
    }
  }

  public enum Config {

    VIEW("view.cfg"), APPLICATION("application.cfg");

    public final String fileName;

    Config(String fileName) {
      this.fileName = fileName;
    }
  }
}
