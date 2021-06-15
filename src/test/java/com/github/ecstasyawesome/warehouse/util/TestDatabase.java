package com.github.ecstasyawesome.warehouse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabase {

  public static Connection getConnection() throws SQLException {
    var connection = getConnectionToInMemoryDatabase();
    DatabaseManager.createTablesIfAbsent(connection);
    return connection.isClosed() ? getConnectionToInMemoryDatabase() : connection;
  }

  private static Connection getConnectionToInMemoryDatabase() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
  }

}
