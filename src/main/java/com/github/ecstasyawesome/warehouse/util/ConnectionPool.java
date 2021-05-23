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
      CREATE TABLE IF NOT EXISTS USERS
      (
          USER_ID          BIGINT      NOT NULL AUTO_INCREMENT,
          USER_SURNAME     VARCHAR(25) NOT NULL,
          USER_NAME        VARCHAR(25) NOT NULL,
          USER_SECOND_NAME VARCHAR(25) NOT NULL,
          CONSTRAINT PK_USER_ID PRIMARY KEY (USER_ID)
      )
      """;
  private static final String USERS_CONTACTS_TABLE = """
      CREATE TABLE IF NOT EXISTS USERS_CONTACTS
      (
          USER_CONTACT_ID    BIGINT      NOT NULL AUTO_INCREMENT,
          USER_ID            BIGINT      NOT NULL UNIQUE,
          USER_CONTACT_PHONE VARCHAR(20) NOT NULL,
          USER_CONTACT_EMAIL VARCHAR(40),
          CONSTRAINT PK_USER_CONTACT_ID PRIMARY KEY (USER_CONTACT_ID),
          CONSTRAINT FK_USER_CONTACT_USER_ID
              FOREIGN KEY (USER_ID)
                  REFERENCES USERS (USER_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String USERS_SECURITY_TABLE = """
      CREATE TABLE IF NOT EXISTS USERS_SECURITY
      (
          USER_SECURITY_ID       BIGINT      NOT NULL AUTO_INCREMENT,
          USER_ID                BIGINT      NOT NULL UNIQUE,
          USER_SECURITY_LOGIN    VARCHAR(20) NOT NULL UNIQUE,
          USER_SECURITY_PASSWORD VARCHAR(20) NOT NULL,
          USER_SECURITY_ACCESS   VARCHAR(15) NOT NULL,
          CONSTRAINT PK_USER_SECURITY_ID PRIMARY KEY (USER_SECURITY_ID),
          CONSTRAINT FK_USER_SECURITY_USER_ID
              FOREIGN KEY (USER_ID)
                  REFERENCES USERS (USER_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String CATEGORIES_TABLE = """
      CREATE TABLE IF NOT EXISTS CATEGORIES
      (
          CATEGORY_ID          BIGINT      NOT NULL AUTO_INCREMENT,
          CATEGORY_NAME        VARCHAR(30) NOT NULL UNIQUE,
          CATEGORY_DESCRIPTION VARCHAR(150),
          CONSTRAINT PK_CATEGORY_ID PRIMARY KEY (CATEGORY_ID)
      )
      """;
  private static final String PRODUCTS_TABLE = """
      CREATE TABLE IF NOT EXISTS PRODUCTS
      (
          PRODUCT_ID   BIGINT      NOT NULL AUTO_INCREMENT,
          CATEGORY_ID  BIGINT      NOT NULL,
          PRODUCT_NAME VARCHAR(40) NOT NULL UNIQUE,
          PRODUCT_UNIT VARCHAR(15) NOT NULL,
          CONSTRAINT PK_PRODUCT_ID PRIMARY KEY (PRODUCT_ID),
          CONSTRAINT FK_CATEGORY_ID
              FOREIGN KEY (CATEGORY_ID)
                  REFERENCES CATEGORIES (CATEGORY_ID)
                  ON DELETE CASCADE
      )
      """;

  static {
    createTablesIfAbsent(USERS_TABLE, USERS_CONTACTS_TABLE, USERS_SECURITY_TABLE, CATEGORIES_TABLE,
        PRODUCTS_TABLE);
  }

  public static Connection getConnection() throws SQLException {
    LOGGER.debug("Establishing connection with database");
    return DriverManager.getConnection("jdbc:h2:./database/default", "root", "root");
  }

  private static void createTablesIfAbsent(String... queries) {
    try (var connection = getConnection();
        var statement = connection.createStatement()) {
      connection.setAutoCommit(false);
      for (var query : queries) {
        statement.addBatch(query);
      }
      statement.executeBatch();
      connection.commit();
      LOGGER.debug("Database tables are checked [{} tables]", queries.length);
    } catch (SQLException exception) {
      throw new ExceptionInInitializerError(LOGGER.throwing(Level.FATAL, exception));
    }
  }
}
