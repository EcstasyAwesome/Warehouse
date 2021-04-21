package com.github.ecstasyawesome.warehouse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

  private static final String USERS_TABLE = """
      CREATE TABLE USERS (
          ID          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
          SURNAME     VARCHAR(25) NOT NULL,
          NAME        VARCHAR(25) NOT NULL,
          SECOND_NAME VARCHAR(25) NOT NULL,
          PHONE       VARCHAR(20) NOT NULL,
          LOGIN       VARCHAR(20) NOT NULL UNIQUE,
          PASSWORD    VARCHAR(20) NOT NULL,
          ACCESS      VARCHAR(15) NOT NULL
      )
      """;

  static {
    System.setProperty("derby.language.sequence.preallocator", "1");
    createTableIfExist(USERS_TABLE);
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:derby:database;create=true");
  }

  private static void createTableIfExist(String sql) {
    try (var connection = getConnection();
        var statement = connection.prepareStatement(sql)) {
      statement.execute();
    } catch (SQLException ignored) { // TODO maybe should write some information to log
    }
  }
}
