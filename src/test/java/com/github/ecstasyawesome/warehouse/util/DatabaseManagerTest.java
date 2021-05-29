package com.github.ecstasyawesome.warehouse.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringJoiner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DatabaseManagerTest {

  private static final Path DB_PATH = Path.of("database");
  private static final Path DB_TEMP_PATH = Path.of("databaseTmp");
  private static boolean isCreatedDbFolder = false;

  @BeforeAll
  public static void beforeAll() throws IOException {
    try {
      Files.walk(DB_TEMP_PATH)
          .sorted(Comparator.reverseOrder())
          .map(Path::toFile)
          .forEach(File::delete);
      // to backup data, if db is exist and used for development
      Files.move(DB_PATH, DB_TEMP_PATH, StandardCopyOption.REPLACE_EXISTING);
      isCreatedDbFolder = true;
    } catch (NoSuchFileException ignored) {
    }
  }

  @AfterAll
  public static void afterAll() throws IOException {
    if (isCreatedDbFolder) {
      Files.walk(DB_PATH)
          .sorted(Comparator.reverseOrder())
          .map(Path::toFile)
          .forEach(File::delete);
      // to restore data
      Files.move(DB_TEMP_PATH, DB_PATH, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  @Test
  public void databaseCreatedAndConnectionAvailable() {
    try (var connection = DatabaseManager.getConnection()) {
      assertNotNull(connection);
    } catch (SQLException exception) {
      fail(exception);
    }
    assertTrue(Files.exists(Path.of("database", "default.mv.db")));
  }

  @Test
  public void tablesSuccessfullyChecked() {
    var expected = new String[]{"CATEGORIES", "COMPANIES", "COMPANIES_ADDRESSES",
        "COMPANIES_CONTACTS", "PRODUCTS", "PRODUCT_PROVIDERS", "PRODUCT_PROVIDERS_ADDRESSES",
        "PRODUCT_PROVIDERS_CONTACTS", "PRODUCT_STORAGES", "PRODUCT_STORAGES_ADDRESSES",
        "PRODUCT_STORAGES_CONTACTS", "USERS", "USERS_CONTACTS", "USERS_SECURITY"};
    try (var connection = TestConnectionPool.getConnection()) {
      var metaData = connection.getMetaData();
      try (var resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
        var joiner = new StringJoiner(";");
        while (resultSet.next()) {
          joiner.add(resultSet.getString("TABLE_NAME"));
        }
        var actual = joiner.toString().split(";");
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
      }
    } catch (Throwable throwable) {
      fail(throwable);
    }
  }
}