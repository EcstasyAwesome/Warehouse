package com.github.ecstasyawesome.warehouse.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.ecstasyawesome.warehouse.ResourceBackupManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.StringJoiner;
import org.junit.jupiter.api.Test;

public class DatabaseManagerTest {

  @Test
  public void databaseCreated() throws IOException {
    var databasePath = Path.of("database");
    var resourceManager = new ResourceBackupManager(databasePath);
    resourceManager.backup();
    assertTrue(Files.notExists(databasePath));
    var expected = new String[]{"CATEGORIES", "COMPANIES", "COMPANIES_ADDRESSES", "ORDERS_ITEMS",
        "COMPANIES_CONTACTS", "PRODUCTS", "PRODUCT_PROVIDERS", "PRODUCT_PROVIDERS_ADDRESSES",
        "PRODUCT_PROVIDERS_CONTACTS", "PRODUCT_STORAGES", "PRODUCT_STORAGES_ADDRESSES", "ORDERS",
        "PRODUCT_STORAGES_CONTACTS", "USERS", "USERS_CONTACTS", "USERS_SECURITY"};
    try (var connection = DatabaseManager.getConnection()) {
      DatabaseManager.createTablesIfAbsent(connection);
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
    assertTrue(Files.exists(databasePath.resolve("default.mv.db")));
    resourceManager.restore();
  }
}