package com.github.ecstasyawesome.warehouse.util;

import com.github.ecstasyawesome.warehouse.service.EventManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import javafx.scene.control.Alert.AlertType;

public final class PropertyTool {

  private static final Path ROOT = Path.of("settings");

  private PropertyTool() {
  }

  public static Properties load(Config config) {
    final var pathToFile = preparePath(config);
    final var result = new Properties();
    try (var inputStream = Files.newInputStream(pathToFile)) {
      result.load(inputStream);
    } catch (IOException exception) {
      EventManager.showPopUpWindow(AlertType.WARNING, exception.getMessage());  // TODO message
    }
    return result;
  }

  public static void save(Config config, final Properties properties) {
    Objects.requireNonNull(properties);
    verifyRootFolder();
    final var pathToFile = preparePath(config);
    try (var outputStream = Files.newOutputStream(pathToFile)) {
      properties.store(outputStream, "DO NOT MODIFY THIS FILE");
    } catch (IOException exception) {
      EventManager.showPopUpWindow(AlertType.ERROR, exception.getMessage());  // TODO message
    }
  }

  private static Path preparePath(Config config) {
    return ROOT.resolve(config.fileName);
  }

  private static void verifyRootFolder() {
    if (Files.notExists(ROOT)) {
      try {
        Files.createDirectory(ROOT);
      } catch (IOException exception) {
        EventManager.showPopUpWindow(AlertType.ERROR, exception.getMessage());  // TODO message
      }
    }
  }

  public enum Config {

    VIEW("view.cfg"), SETTINGS("settings.cfg");

    private final String fileName;

    Config(String fileName) {
      this.fileName = fileName;
    }
  }
}
