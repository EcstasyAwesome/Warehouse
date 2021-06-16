package com.github.ecstasyawesome.warehouse.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.ecstasyawesome.warehouse.ResourceBackupManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.StringJoiner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseManagerTest {

  private static final Path DB_PATH = Path.of("database");
  private final ResourceBackupManager BACKUP_MANAGER = new ResourceBackupManager(DB_PATH);

  @BeforeEach
  public void setUp() throws IOException {
    BACKUP_MANAGER.backup();
  }

  @AfterEach
  public void tearDown() throws IOException {
    BACKUP_MANAGER.restore();
  }

  @Test
  public void databaseCreatedAndConnectionAvailable() {
    assertTrue(Files.notExists(DB_PATH));
    try (var connection = DatabaseManager.getConnection()) {
      assertNotNull(connection);
    } catch (SQLException exception) {
      fail(exception);
    }
    assertTrue(Files.exists(DB_PATH.resolve("default.mv.db")));
  }

  @Test
  public void tablesSuccessfullyChecked() {
    assertTrue(Files.notExists(DB_PATH));
    var expected = new String[]{"CATEGORIES", "COMPANIES", "COMPANIES_ADDRESSES", "ORDERS_ITEMS",
        "COMPANIES_CONTACTS", "PRODUCTS", "PRODUCT_PROVIDERS", "PRODUCT_PROVIDERS_ADDRESSES",
        "PRODUCT_PROVIDERS_CONTACTS", "PRODUCT_STORAGES", "PRODUCT_STORAGES_ADDRESSES", "ORDERS",
        "PRODUCT_STORAGES_CONTACTS", "USERS", "USERS_CONTACTS", "USERS_SECURITY"};
    try (var connection = DatabaseManager.getConnection()) {
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