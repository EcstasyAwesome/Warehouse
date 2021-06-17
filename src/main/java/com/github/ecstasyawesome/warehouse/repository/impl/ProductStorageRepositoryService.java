package com.github.ecstasyawesome.warehouse.repository.impl;

import com.github.ecstasyawesome.warehouse.model.impl.Address;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.repository.ProductStorageRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductStorageRepositoryService extends ProductStorageRepository {

  private static final ProductStorageRepositoryService INSTANCE =
      new ProductStorageRepositoryService();
  private final CompanyRepositoryService companyRepositoryService =
      CompanyRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(ProductStorageRepositoryService.class);

  private ProductStorageRepositoryService() {
  }

  public static ProductStorageRepositoryService getInstance() {
    return INSTANCE;
  }

  @Override
  public ObservableList<ProductStorage> getAll(Company criteria) {
    Objects.requireNonNull(criteria);
    final var query = """
        SELECT *
        FROM PRODUCT_STORAGES
            INNER JOIN PRODUCT_STORAGES_CONTACTS
                ON PRODUCT_STORAGES.STORAGE_ID = PRODUCT_STORAGES_CONTACTS.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_ADDRESSES
                ON PRODUCT_STORAGES.STORAGE_ID = PRODUCT_STORAGES_ADDRESSES.STORAGE_ID
            INNER JOIN COMPANIES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES.COMPANY_ID
            INNER JOIN COMPANIES_CONTACTS
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_CONTACTS.COMPANY_ID
            INNER JOIN COMPANIES_ADDRESSES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_ADDRESSES.COMPANY_ID
        WHERE PRODUCT_STORAGES.COMPANY_ID=?
        """;
    try {
      var result = selectRecords(query, criteria.getId());
      logger.debug("Selected all product storages by company id={} [{} record(s)]",
          criteria.getId(), result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<ProductStorage> getAll() {
    final var query = """
        SELECT *
        FROM PRODUCT_STORAGES
            INNER JOIN PRODUCT_STORAGES_CONTACTS
                ON PRODUCT_STORAGES.STORAGE_ID = PRODUCT_STORAGES_CONTACTS.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_ADDRESSES
                ON PRODUCT_STORAGES.STORAGE_ID = PRODUCT_STORAGES_ADDRESSES.STORAGE_ID
            INNER JOIN COMPANIES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES.COMPANY_ID
            INNER JOIN COMPANIES_CONTACTS
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_CONTACTS.COMPANY_ID
            INNER JOIN COMPANIES_ADDRESSES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_ADDRESSES.COMPANY_ID
        """;
    try {
      var result = selectRecords(query);
      logger.debug("Selected all product storages [{} record(s)]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void create(final ProductStorage instance) {
    Objects.requireNonNull(instance);
    final var productStorageQuery = """
        INSERT INTO PRODUCT_STORAGES (COMPANY_ID, STORAGE_NAME)
        VALUES (?, ?)
        """;
    final var productStorageContactQuery = """
        INSERT INTO PRODUCT_STORAGES_CONTACTS (STORAGE_ID, CONTACT_PHONE, CONTACT_EXTRA_PHONE,
                                               CONTACT_EMAIL, CONTACT_SITE)
        VALUES (?, ?, ?, ?, ?)
        """;
    final var productStorageAddressQuery = """
        INSERT INTO PRODUCT_STORAGES_ADDRESSES (STORAGE_ID, ADDRESS_REGION, ADDRESS_TOWN,
                                                ADDRESS_STREET, ADDRESS_NUMBER)
        VALUES (?, ?, ?, ?, ?)
        """;
    try (var connection = DatabaseManager.getConnection()) {
      connection.setAutoCommit(false);
      try {
        final var storageId = insertRecord(connection, productStorageQuery,
            instance.getCompany().getId(), instance.getName());
        final var contact = instance.getBusinessContact();
        final var contactId = insertRecord(connection, productStorageContactQuery, storageId,
            contact.getPhone(), contact.getExtraPhone(), contact.getEmail(), contact.getSite());
        final var address = instance.getAddress();
        final var addressId = insertRecord(connection, productStorageAddressQuery, storageId,
            address.getRegion(), address.getTown(), address.getStreet(), address.getNumber());
        connection.commit();
        contact.setId(contactId);
        address.setId(addressId);
        instance.setId(storageId);
        logger.debug("Created a new product storage with id={}", storageId);
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ProductStorage read(final long id) {
    final var query = """
        SELECT *
        FROM PRODUCT_STORAGES
            INNER JOIN PRODUCT_STORAGES_CONTACTS
                ON PRODUCT_STORAGES.STORAGE_ID = PRODUCT_STORAGES_CONTACTS.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_ADDRESSES
                ON PRODUCT_STORAGES.STORAGE_ID = PRODUCT_STORAGES_ADDRESSES.STORAGE_ID
            INNER JOIN COMPANIES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES.COMPANY_ID
            INNER JOIN COMPANIES_CONTACTS
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_CONTACTS.COMPANY_ID
            INNER JOIN COMPANIES_ADDRESSES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_ADDRESSES.COMPANY_ID
        WHERE PRODUCT_STORAGES.STORAGE_ID=?
        """;
    try {
      var result = selectRecord(query, id);
      logger.debug("Selected a product storage with id={}", result.getId());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(final ProductStorage instance) {
    Objects.requireNonNull(instance);
    final var query = """
        UPDATE PRODUCT_STORAGES_CONTACTS
        SET CONTACT_PHONE=?,
            CONTACT_EXTRA_PHONE=?,
            CONTACT_EMAIL=?,
            CONTACT_SITE=?
        WHERE CONTACT_ID=?;
        UPDATE PRODUCT_STORAGES_ADDRESSES
        SET ADDRESS_REGION=?,
            ADDRESS_TOWN=?,
            ADDRESS_STREET=?,
            ADDRESS_NUMBER=?
        WHERE ADDRESS_ID=?;
        UPDATE PRODUCT_STORAGES
        SET STORAGE_NAME=?,
            COMPANY_ID=?
        WHERE STORAGE_ID=?
        """;
    final var contact = instance.getBusinessContact();
    final var address = instance.getAddress();
    try {
      execute(query, contact.getPhone(), contact.getExtraPhone(), contact.getEmail(),
          contact.getSite(), contact.getId(), address.getRegion(), address.getTown(),
          address.getStreet(), address.getNumber(), address.getId(), instance.getName(),
          instance.getCompany().getId(), instance.getId());
      logger.debug("Updated a product storage with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final ProductStorage instance) {
    try {
      var result = execute("DELETE FROM PRODUCT_STORAGES WHERE STORAGE_ID=?", instance.getId());
      if (result == 0) {
        throw new SQLException("Deleted nothing");
      }
      logger.debug("Deleted a product storage with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected ProductStorage transformToObj(final ResultSet resultSet) throws SQLException {
    var company = companyRepositoryService.transformToObj(resultSet);
    var contact = BusinessContact.Builder.create()
        .setId(resultSet.getLong("PRODUCT_STORAGES_CONTACTS.CONTACT_ID"))
        .setPhone(resultSet.getString("PRODUCT_STORAGES_CONTACTS.CONTACT_PHONE"))
        .setExtraPhone(resultSet.getString("PRODUCT_STORAGES_CONTACTS.CONTACT_EXTRA_PHONE"))
        .setEmail(resultSet.getString("PRODUCT_STORAGES_CONTACTS.CONTACT_EMAIL"))
        .setSite(resultSet.getString("PRODUCT_STORAGES_CONTACTS.CONTACT_SITE"))
        .build();
    var address = Address.Builder.create()
        .setId(resultSet.getLong("PRODUCT_STORAGES_ADDRESSES.ADDRESS_ID"))
        .setRegion(resultSet.getString("PRODUCT_STORAGES_ADDRESSES.ADDRESS_REGION"))
        .setTown(resultSet.getString("PRODUCT_STORAGES_ADDRESSES.ADDRESS_TOWN"))
        .setStreet(resultSet.getString("PRODUCT_STORAGES_ADDRESSES.ADDRESS_STREET"))
        .setNumber(resultSet.getString("PRODUCT_STORAGES_ADDRESSES.ADDRESS_NUMBER"))
        .build();
    return ProductStorage.Builder.create()
        .setId(resultSet.getLong("PRODUCT_STORAGES.STORAGE_ID"))
        .setName(resultSet.getString("PRODUCT_STORAGES.STORAGE_NAME"))
        .setCompany(company)
        .setBusinessContact(contact)
        .setAddress(address)
        .build();
  }
}
