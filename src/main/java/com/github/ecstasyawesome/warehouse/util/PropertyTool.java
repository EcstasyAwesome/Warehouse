package com.github.ecstasyawesome.warehouse.util;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PropertyTool {

  private static final WindowManager WINDOW_MANAGER = WindowManager.getInstance();
  private static final Logger LOGGER = LogManager.getLogger(PropertyTool.class);
  private static final Path ROOT = Path.of("settings");

  private PropertyTool() {
  }

  public static Properties load(Config config) {
    final var pathToFile = preparePath(config);
    final var result = new Properties();
    try (var inputStream = Files.newInputStream(pathToFile)) {
      result.load(inputStream);
      LOGGER.debug(String.format("'%s' config loaded", config.fileName));
    } catch (IOException exception) {
      LOGGER.warn(String.format("'%s' config is absent", config.fileName));
    }
    return result;
  }

  public static void save(Config config, final Properties properties) {
    Objects.requireNonNull(properties);
    verifyRootFolder();
    final var pathToFile = preparePath(config);
    try (var outputStream = Files.newOutputStream(pathToFile)) {
      properties.store(outputStream, "DO NOT MODIFY THIS FILE");
      LOGGER.debug(String.format("'%s' config saved", config.fileName));
    } catch (IOException exception) {
      var message = String.format("Cannot save the config '%s'", config.fileName);
      LOGGER.error(message, exception);
      WINDOW_MANAGER.showDialog(exception);
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
        WINDOW_MANAGER.showDialog(exception);
      }
    }
  }

  public enum Config {

    VIEW("view.cfg"), APPLICATION("application.cfg");

    private final String fileName;

    Config(String fileName) {
      this.fileName = fileName;
    }
  }
}
