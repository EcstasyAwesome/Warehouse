package com.github.ecstasyawesome.warehouse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConnectionPool {

  private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
  private static final String USERS_TABLE = """
      CREATE TABLE USERS (
          ID          BIGINT PRIMARY KEY AUTO_INCREMENT,
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
    createTableIfExist(USERS_TABLE);
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:./database/default", "root", "root");
  }

  private static void createTableIfExist(String sql) {
    try (var connection = getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(sql)) {
        statement.execute();
        connection.commit();
        LOGGER.debug("Table created"); // TODO table name
      } catch (SQLException exception) {
        connection.rollback();
        LOGGER.debug("Table exists"); // TODO table name
      }
    } catch (SQLException exception) {
      throw new ExceptionInInitializerError(LOGGER.throwing(Level.FATAL, exception));
    }
  }
}
