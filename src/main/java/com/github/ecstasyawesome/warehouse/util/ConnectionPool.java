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
      CREATE TABLE USERS
      (
          USER_ID          BIGINT PRIMARY KEY AUTO_INCREMENT,
          USER_SURNAME     VARCHAR(25) NOT NULL,
          USER_NAME        VARCHAR(25) NOT NULL,
          USER_SECOND_NAME VARCHAR(25) NOT NULL,
          USER_PHONE       VARCHAR(20) NOT NULL,
          USER_LOGIN       VARCHAR(20) NOT NULL UNIQUE,
          USER_PASSWORD    VARCHAR(20) NOT NULL,
          USER_ACCESS      VARCHAR(15) NOT NULL
      )
      """;
  private static final String CATEGORIES_TABLE = """
      CREATE TABLE CATEGORIES
      (
          CATEGORY_ID   BIGINT PRIMARY KEY AUTO_INCREMENT,
          CATEGORY_NAME VARCHAR(30) NOT NULL UNIQUE
      )
      """;
  private static final String PRODUCTS_TABLE = """
      CREATE TABLE PRODUCTS
      (
          PRODUCT_ID   BIGINT PRIMARY KEY AUTO_INCREMENT,
          PRODUCT_NAME VARCHAR(40) NOT NULL UNIQUE,
          PRODUCT_UNIT VARCHAR(15) NOT NULL,
          CATEGORY_ID  BIGINT      NOT NULL,
          CONSTRAINT FK_CATEGORY_ID
              FOREIGN KEY (CATEGORY_ID)
                  REFERENCES CATEGORIES (CATEGORY_ID)
                  ON DELETE CASCADE
      )
      """;

  static {
    createTableIfExist(USERS_TABLE, "USERS");
    createTableIfExist(CATEGORIES_TABLE, "CATEGORIES");
    createTableIfExist(PRODUCTS_TABLE, "PRODUCTS");
  }

  public static Connection getConnection() throws SQLException {
    LOGGER.debug("Establishing connection with database");
    return DriverManager.getConnection("jdbc:h2:./database/default", "root", "root");
  }

  private static void createTableIfExist(String sql, String tableName) {
    try (var connection = getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(sql)) {
        statement.execute();
        connection.commit();
        LOGGER.debug("Table created [{}]", tableName);
      } catch (SQLException exception) {
        connection.rollback();
        LOGGER.debug("Table exists [{}]", tableName);
      }
    } catch (SQLException exception) {
      throw new ExceptionInInitializerError(LOGGER.throwing(Level.FATAL, exception));
    }
  }
}
