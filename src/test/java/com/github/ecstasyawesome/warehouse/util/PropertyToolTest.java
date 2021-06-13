package com.github.ecstasyawesome.warehouse.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ecstasyawesome.warehouse.ResourceBackupManager;
import com.github.ecstasyawesome.warehouse.util.PropertyTool.Config;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PropertyToolTest {

  private static final Path SETTINGS_PATH = Path.of("settings");
  private static final ResourceBackupManager BACKUP_MANAGER =
      new ResourceBackupManager(SETTINGS_PATH);

  @BeforeAll
  public static void beforeAll() throws IOException {
    BACKUP_MANAGER.backup();
  }

  @AfterAll
  public static void afterAll() throws IOException {
    BACKUP_MANAGER.restore();
  }

  @AfterEach
  public void tearDown() throws IOException {
    ResourceBackupManager.deleteAllFiles(SETTINGS_PATH);
  }

  @Test
  public void loadProperties() throws IOException {
    var expected = new Properties();
    expected.setProperty("key", "value");
    Files.createDirectory(SETTINGS_PATH);
    try (var output = Files.newOutputStream(SETTINGS_PATH.resolve(Config.VIEW.fileName))) {
      expected.store(output, null);
    }
    var actual = PropertyTool.load(Config.VIEW);
    assertFalse(actual.isEmpty());
    assertEquals(expected, actual);
  }

  @Test
  public void loadPropertiesIfAbsent() {
    var result = PropertyTool.load(Config.VIEW);
    assertTrue(result.isEmpty());
  }

  @Test
  public void loadPropertiesHasNoExceptionIfAbsent() {
    assertDoesNotThrow(() -> PropertyTool.load(Config.APPLICATION));
  }

  @Test
  public void loadPropertiesThrowsNpeIfConfigNull() {
    assertThrows(NullPointerException.class, () -> PropertyTool.load(null));
  }

  @Test
  public void savePropertiesThrowsNpeIfPropsNull() {
    assertThrows(NullPointerException.class, () -> PropertyTool.save(Config.VIEW, null));
  }

  @Test
  public void savePropertiesThrowsNpeIfConfigNull() {
    assertThrows(NullPointerException.class, () -> PropertyTool.save(null, new Properties()));
  }

  @Test
  public void savePropertiesCreatesDirAndFile() {
    var props = new Properties();
    props.setProperty("key", "value");
    PropertyTool.save(Config.VIEW, props);
    assertTrue(Files.exists(SETTINGS_PATH.resolve(Config.VIEW.fileName)));
  }

  @Test
  public void savePropertiesWritesAllDataRight() throws IOException {
    var expected = new Properties();
    expected.setProperty("key", "value");
    PropertyTool.save(Config.VIEW, expected);
    var actual = new Properties();
    try (var input = Files.newInputStream(SETTINGS_PATH.resolve(Config.VIEW.fileName))) {
      actual.load(input);
    }
    assertFalse(actual.isEmpty());
    assertEquals(expected, actual);
  }
}