package com.github.ecstasyawesome.warehouse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionPool {

  static {
    try (var connection = TestConnectionPool.getConnection()) {
      DatabaseManager.createTablesIfAbsent(connection);
    } catch (SQLException exception) {
      throw new ExceptionInInitializerError(exception);
    }
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
  }

}
