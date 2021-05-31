package com.github.ecstasyawesome.warehouse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DatabaseManager {

  private static final Logger LOGGER = LogManager.getLogger(DatabaseManager.class);
  private static final String USERS_TABLE = """
      CREATE TABLE IF NOT EXISTS USERS
      (
          USER_ID          BIGINT      NOT NULL AUTO_INCREMENT,
          USER_SURNAME     VARCHAR(30) NOT NULL,
          USER_NAME        VARCHAR(30) NOT NULL,
          USER_SECOND_NAME VARCHAR(30) NOT NULL,
          CONSTRAINT PK_USER_ID PRIMARY KEY (USER_ID)
      )
      """;
  private static final String USERS_CONTACTS_TABLE = """
      CREATE TABLE IF NOT EXISTS USERS_CONTACTS
      (
          CONTACT_ID    BIGINT      NOT NULL AUTO_INCREMENT,
          USER_ID       BIGINT      NOT NULL UNIQUE,
          CONTACT_PHONE VARCHAR(20) NOT NULL,
          CONTACT_EMAIL VARCHAR(40),
          CONSTRAINT PK_USER_CONTACT_ID PRIMARY KEY (CONTACT_ID),
          CONSTRAINT FK_USER_CONTACT_USER_ID
              FOREIGN KEY (USER_ID)
                  REFERENCES USERS (USER_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String USERS_SECURITY_TABLE = """
      CREATE TABLE IF NOT EXISTS USERS_SECURITY
      (
          SECURITY_ID       BIGINT      NOT NULL AUTO_INCREMENT,
          USER_ID           BIGINT      NOT NULL UNIQUE,
          SECURITY_LOGIN    VARCHAR(20) NOT NULL UNIQUE,
          SECURITY_PASSWORD VARCHAR(20) NOT NULL,
          SECURITY_ACCESS   VARCHAR(15) NOT NULL,
          CONSTRAINT PK_USER_SECURITY_ID PRIMARY KEY (SECURITY_ID),
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
          CATEGORY_NAME        VARCHAR(40) NOT NULL UNIQUE,
          CATEGORY_DESCRIPTION VARCHAR(150),
          CONSTRAINT PK_CATEGORY_ID PRIMARY KEY (CATEGORY_ID)
      )
      """;
  private static final String PRODUCTS_TABLE = """
      CREATE TABLE IF NOT EXISTS PRODUCTS
      (
          PRODUCT_ID   BIGINT      NOT NULL AUTO_INCREMENT,
          CATEGORY_ID  BIGINT      NOT NULL,
          PRODUCT_NAME VARCHAR(60) NOT NULL UNIQUE,
          PRODUCT_UNIT VARCHAR(10) NOT NULL,
          CONSTRAINT PK_PRODUCT_ID PRIMARY KEY (PRODUCT_ID),
          CONSTRAINT FK_PRODUCT_CATEGORY_ID
              FOREIGN KEY (CATEGORY_ID)
                  REFERENCES CATEGORIES (CATEGORY_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String COMPANIES_TABLE = """
      CREATE TABLE IF NOT EXISTS COMPANIES
      (
          COMPANY_ID              BIGINT      NOT NULL AUTO_INCREMENT,
          COMPANY_NAME            VARCHAR(60) NOT NULL UNIQUE,
          COMPANY_IDENTIFIER_CODE VARCHAR(20) NOT NULL UNIQUE,
          COMPANY_PERSON_TYPE     VARCHAR(20) NOT NULL,
          CONSTRAINT PK_COMPANY_ID PRIMARY KEY (COMPANY_ID)
      )
      """;
  private static final String COMPANIES_CONTACTS_TABLE = """
      CREATE TABLE IF NOT EXISTS COMPANIES_CONTACTS
      (
          CONTACT_ID          BIGINT      NOT NULL AUTO_INCREMENT,
          COMPANY_ID          BIGINT      NOT NULL UNIQUE,
          CONTACT_PHONE       VARCHAR(20) NOT NULL,
          CONTACT_EXTRA_PHONE VARCHAR(20),
          CONTACT_EMAIL       VARCHAR(40),
          CONTACT_SITE        VARCHAR(40),
          CONSTRAINT PK_COMPANY_CONTACT_ID PRIMARY KEY (CONTACT_ID),
          CONSTRAINT FK_COMPANY_CONTACT_COMPANY_ID
              FOREIGN KEY (COMPANY_ID)
                  REFERENCES COMPANIES (COMPANY_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String COMPANIES_ADDRESSES_TABLE = """
      CREATE TABLE IF NOT EXISTS COMPANIES_ADDRESSES
      (
          ADDRESS_ID     BIGINT      NOT NULL AUTO_INCREMENT,
          COMPANY_ID     BIGINT      NOT NULL UNIQUE,
          ADDRESS_REGION VARCHAR(60) NOT NULL,
          ADDRESS_TOWN   VARCHAR(40) NOT NULL,
          ADDRESS_STREET VARCHAR(60),
          ADDRESS_NUMBER VARCHAR(10),
          CONSTRAINT PK_COMPANY_ADDRESS_ID PRIMARY KEY (ADDRESS_ID),
          CONSTRAINT FK_COMPANY_ADDRESS_COMPANY_ID
              FOREIGN KEY (COMPANY_ID)
                  REFERENCES COMPANIES (COMPANY_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String PRODUCT_STORAGES_TABLE = """
      CREATE TABLE IF NOT EXISTS PRODUCT_STORAGES
      (
          STORAGE_ID   BIGINT      NOT NULL AUTO_INCREMENT,
          COMPANY_ID   BIGINT      NOT NULL,
          STORAGE_NAME VARCHAR(60) NOT NULL UNIQUE,
          CONSTRAINT PK_PRODUCT_STORAGE_ID PRIMARY KEY (STORAGE_ID),
          CONSTRAINT FK_PRODUCT_STORAGE_COMPANY_ID
              FOREIGN KEY (COMPANY_ID)
                  REFERENCES COMPANIES (COMPANY_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String PRODUCT_STORAGES_CONTACTS_TABLE = """
      CREATE TABLE IF NOT EXISTS PRODUCT_STORAGES_CONTACTS
      (
          CONTACT_ID          BIGINT      NOT NULL AUTO_INCREMENT,
          STORAGE_ID          BIGINT      NOT NULL UNIQUE,
          CONTACT_PHONE       VARCHAR(20) NOT NULL,
          CONTACT_EXTRA_PHONE VARCHAR(20),
          CONTACT_EMAIL       VARCHAR(40),
          CONTACT_SITE        VARCHAR(40),
          CONSTRAINT PK_PRODUCT_STORAGE_CONTACT_ID PRIMARY KEY (CONTACT_ID),
          CONSTRAINT FK_PRODUCT_STORAGE_CONTACT_STORAGE_ID
              FOREIGN KEY (STORAGE_ID)
                  REFERENCES PRODUCT_STORAGES (STORAGE_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String PRODUCT_STORAGES_ADDRESSES_TABLE = """
      CREATE TABLE IF NOT EXISTS PRODUCT_STORAGES_ADDRESSES
      (
          ADDRESS_ID     BIGINT      NOT NULL AUTO_INCREMENT,
          STORAGE_ID     BIGINT      NOT NULL UNIQUE,
          ADDRESS_REGION VARCHAR(60) NOT NULL,
          ADDRESS_TOWN   VARCHAR(40) NOT NULL,
          ADDRESS_STREET VARCHAR(60),
          ADDRESS_NUMBER VARCHAR(10),
          CONSTRAINT PK_PRODUCT_STORAGE_ADDRESS_ID PRIMARY KEY (ADDRESS_ID),
          CONSTRAINT FK_PRODUCT_STORAGE_ADDRESS_STORAGE_ID
              FOREIGN KEY (STORAGE_ID)
                  REFERENCES PRODUCT_STORAGES (STORAGE_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String PRODUCT_PROVIDERS_TABLE = """
      CREATE TABLE IF NOT EXISTS PRODUCT_PROVIDERS
      (
          PROVIDER_ID   BIGINT      NOT NULL AUTO_INCREMENT,
          PROVIDER_NAME VARCHAR(60) NOT NULL UNIQUE,
          CONSTRAINT PK_PRODUCT_PROVIDER_ID PRIMARY KEY (PROVIDER_ID)
      )
      """;
  private static final String PRODUCT_PROVIDERS_CONTACTS_TABLE = """
      CREATE TABLE IF NOT EXISTS PRODUCT_PROVIDERS_CONTACTS
      (
          CONTACT_ID          BIGINT      NOT NULL AUTO_INCREMENT,
          PROVIDER_ID         BIGINT      NOT NULL UNIQUE,
          CONTACT_PHONE       VARCHAR(20) NOT NULL,
          CONTACT_EXTRA_PHONE VARCHAR(20),
          CONTACT_EMAIL       VARCHAR(40),
          CONTACT_SITE        VARCHAR(40),
          CONSTRAINT PK_PRODUCT_PROVIDER_CONTACT_ID PRIMARY KEY (CONTACT_ID),
          CONSTRAINT FK_PRODUCT_PROVIDER_CONTACT_PROVIDER_ID
              FOREIGN KEY (PROVIDER_ID)
                  REFERENCES PRODUCT_PROVIDERS (PROVIDER_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String PRODUCT_PROVIDERS_ADDRESSES_TABLE = """
      CREATE TABLE IF NOT EXISTS PRODUCT_PROVIDERS_ADDRESSES
      (
          ADDRESS_ID     BIGINT      NOT NULL AUTO_INCREMENT,
          PROVIDER_ID    BIGINT      NOT NULL UNIQUE,
          ADDRESS_REGION VARCHAR(60) NOT NULL,
          ADDRESS_TOWN   VARCHAR(40) NOT NULL,
          ADDRESS_STREET VARCHAR(60),
          ADDRESS_NUMBER VARCHAR(10),
          CONSTRAINT PK_PRODUCT_PROVIDER_ADDRESS_ID PRIMARY KEY (ADDRESS_ID),
          CONSTRAINT FK_PRODUCT_PROVIDER_ADDRESS_PROVIDER_ID
              FOREIGN KEY (PROVIDER_ID)
                  REFERENCES PRODUCT_PROVIDERS (PROVIDER_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String ORDERS_TABLE = """
      CREATE TABLE IF NOT EXISTS ORDERS
      (
          ORDER_ID    BIGINT      NOT NULL AUTO_INCREMENT,
          PROVIDER_ID BIGINT      NOT NULL,
          STORAGE_ID  BIGINT      NOT NULL,
          USER_ID     BIGINT      NOT NULL,
          ORDER_TIME  TIMESTAMP   NOT NULL,
          CONSTRAINT PK_ORDER_ORDER_ID PRIMARY KEY (ORDER_ID),
          CONSTRAINT FK_ORDER_PROVIDER_ID
              FOREIGN KEY (PROVIDER_ID)
                  REFERENCES PRODUCT_PROVIDERS (PROVIDER_ID)
                  ON DELETE CASCADE,
          CONSTRAINT FK_ORDER_STORAGE_ID
              FOREIGN KEY (STORAGE_ID)
                  REFERENCES PRODUCT_STORAGES (STORAGE_ID)
                  ON DELETE CASCADE,
          CONSTRAINT FK_ORDER_USER_ID
              FOREIGN KEY (USER_ID)
                  REFERENCES USERS (USER_ID)
                  ON DELETE RESTRICT
      )
      """;
  private static final String ORDERS_ITEMS_TABLE = """
      CREATE TABLE IF NOT EXISTS ORDERS_ITEMS
      (
          ITEM_ID     BIGINT NOT NULL AUTO_INCREMENT,
          ORDER_ID    BIGINT NOT NULL,
          PRODUCT_ID  BIGINT NOT NULL,
          ITEM_AMOUNT DOUBLE NOT NULL,
          CONSTRAINT PK_ORDER_ITEM_ID PRIMARY KEY (ITEM_ID),
          CONSTRAINT FK_ORDER_ITEM_ORDER_ID
              FOREIGN KEY (ORDER_ID)
                  REFERENCES ORDERS (ORDER_ID)
                  ON DELETE CASCADE,
          CONSTRAINT FK_ORDER_ITEM_PRODUCT_ID
              FOREIGN KEY (PRODUCT_ID)
                  REFERENCES PRODUCTS (PRODUCT_ID)
                  ON DELETE RESTRICT
      )
      """;

  static {
    try (var connection = getConnection()) {
      DatabaseManager.createTablesIfAbsent(connection);
    } catch (SQLException exception) {
      LOGGER.fatal(exception);
    }
  }

  public static Connection getConnection() throws SQLException {
    LOGGER.debug("Establishing connection with database");
    return DriverManager.getConnection("jdbc:h2:./database/default", "root", "root");
  }

  public static void createTablesIfAbsent(final Connection connection) {
    executeQueriesBunch(connection, USERS_TABLE, USERS_CONTACTS_TABLE, USERS_SECURITY_TABLE,
        CATEGORIES_TABLE, PRODUCTS_TABLE, COMPANIES_TABLE, COMPANIES_CONTACTS_TABLE,
        COMPANIES_ADDRESSES_TABLE, PRODUCT_STORAGES_TABLE, PRODUCT_STORAGES_CONTACTS_TABLE,
        PRODUCT_STORAGES_ADDRESSES_TABLE, PRODUCT_PROVIDERS_TABLE, PRODUCT_PROVIDERS_CONTACTS_TABLE,
        PRODUCT_PROVIDERS_ADDRESSES_TABLE, ORDERS_TABLE, ORDERS_ITEMS_TABLE);
  }

  private static void executeQueriesBunch(final Connection connection, String... queries) {
    try (var statement = connection.createStatement()) {
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
