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
          USER_SURNAME     VARCHAR(30) NOT NULL,
          USER_NAME        VARCHAR(30) NOT NULL,
          USER_SECOND_NAME VARCHAR(30) NOT NULL,
          CONSTRAINT PK_USER_ID PRIMARY KEY (USER_ID)
      )
      """;
  private static final String PERSONS_CONTACTS_TABLE = """
      CREATE TABLE IF NOT EXISTS PERSONS_CONTACTS
      (
          PERSON_CONTACT_ID    BIGINT      NOT NULL AUTO_INCREMENT,
          USER_ID              BIGINT      NOT NULL UNIQUE,
          PERSON_CONTACT_PHONE VARCHAR(20) NOT NULL,
          PERSON_CONTACT_EMAIL VARCHAR(40),
          CONSTRAINT PK_PERSON_CONTACT_ID PRIMARY KEY (PERSON_CONTACT_ID),
          CONSTRAINT FK_PERSON_CONTACT_USER_ID
              FOREIGN KEY (USER_ID)
                  REFERENCES USERS (USER_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String SECURITY_TABLE = """
      CREATE TABLE IF NOT EXISTS SECURITY
      (
          SECURITY_ID       BIGINT      NOT NULL AUTO_INCREMENT,
          USER_ID           BIGINT      NOT NULL UNIQUE,
          SECURITY_LOGIN    VARCHAR(20) NOT NULL UNIQUE,
          SECURITY_PASSWORD VARCHAR(20) NOT NULL,
          SECURITY_ACCESS   VARCHAR(15) NOT NULL,
          CONSTRAINT PK_SECURITY_ID PRIMARY KEY (SECURITY_ID),
          CONSTRAINT FK_SECURITY_USER_ID
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
          COMPANY_ID              BIGINT NOT NULL AUTO_INCREMENT,
          COMPANY_NAME            VARCHAR(60) NOT NULL,
          COMPANY_IDENTIFIER_CODE VARCHAR(20) NOT NULL,
          COMPANY_PERSON_TYPE     VARCHAR(20) NOT NULL,
          CONSTRAINT PK_COMPANY_ID PRIMARY KEY (COMPANY_ID)
      )
      """;
  private static final String BUSINESS_CONTACTS_TABLE = """
      CREATE TABLE IF NOT EXISTS BUSINESS_CONTACTS
      (
          BUSINESS_CONTACT_ID          BIGINT      NOT NULL AUTO_INCREMENT,
          COMPANY_ID                   BIGINT      NOT NULL UNIQUE,
          BUSINESS_CONTACT_PHONE       VARCHAR(20) NOT NULL,
          BUSINESS_CONTACT_EXTRA_PHONE VARCHAR(20),
          BUSINESS_CONTACT_EMAIL       VARCHAR(40),
          BUSINESS_CONTACT_SITE        VARCHAR(40),
          CONSTRAINT PK_BUSINESS_CONTACT_ID PRIMARY KEY (BUSINESS_CONTACT_ID),
          CONSTRAINT FK_BUSINESS_CONTACT_COMPANY_ID
              FOREIGN KEY (COMPANY_ID)
                  REFERENCES COMPANIES (COMPANY_ID)
                  ON DELETE CASCADE
      )
      """;
  private static final String ADDRESSES_TABLE = """
      CREATE TABLE IF NOT EXISTS ADDRESSES
      (
          ADDRESS_ID     BIGINT      NOT NULL AUTO_INCREMENT,
          COMPANY_ID     BIGINT      NOT NULL UNIQUE,
          ADDRESS_REGION VARCHAR(60) NOT NULL,
          ADDRESS_TOWN   VARCHAR(40) NOT NULL,
          ADDRESS_STREET VARCHAR(60),
          ADDRESS_NUMBER VARCHAR(10),
          CONSTRAINT PK_ADDRESS_ID PRIMARY KEY (ADDRESS_ID),
          CONSTRAINT FK_ADDRESS_COMPANY_ID
              FOREIGN KEY (COMPANY_ID)
                  REFERENCES COMPANIES (COMPANY_ID)
                  ON DELETE CASCADE
      )
      """;

  static {
    createTablesIfAbsent(USERS_TABLE, PERSONS_CONTACTS_TABLE, SECURITY_TABLE, CATEGORIES_TABLE,
        PRODUCTS_TABLE, COMPANIES_TABLE, BUSINESS_CONTACTS_TABLE, ADDRESSES_TABLE);
  }

  public static void main(String[] args) {

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
