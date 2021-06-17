package com.github.ecstasyawesome.warehouse.repository.impl;

import com.github.ecstasyawesome.warehouse.model.impl.Address;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.repository.ProductProviderRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductProviderRepositoryService extends ProductProviderRepository {

  private static final ProductProviderRepositoryService INSTANCE =
      new ProductProviderRepositoryService();
  private final Logger logger = LogManager.getLogger(ProductProviderRepositoryService.class);

  private ProductProviderRepositoryService() {
  }

  public static ProductProviderRepositoryService getInstance() {
    return INSTANCE;
  }

  @Override
  public ObservableList<ProductProvider> getAll() {
    final var query = """
        SELECT *
        FROM PRODUCT_PROVIDERS
            INNER JOIN PRODUCT_PROVIDERS_CONTACTS
                ON PRODUCT_PROVIDERS.PROVIDER_ID = PRODUCT_PROVIDERS_CONTACTS.PROVIDER_ID
            INNER JOIN PRODUCT_PROVIDERS_ADDRESSES
                ON PRODUCT_PROVIDERS.PROVIDER_ID = PRODUCT_PROVIDERS_ADDRESSES.PROVIDER_ID
        """;
    try {
      var result = selectRecords(query);
      logger.debug("Selected all product providers [{} record(s)]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void create(final ProductProvider instance) {
    Objects.requireNonNull(instance);
    final var productProviderQuery = """
        INSERT INTO PRODUCT_PROVIDERS (PROVIDER_NAME)
        VALUES (?)
        """;
    final var productProviderAddressQuery = """
        INSERT INTO PRODUCT_PROVIDERS_ADDRESSES (PROVIDER_ID, ADDRESS_REGION, ADDRESS_TOWN,
                                                 ADDRESS_STREET, ADDRESS_NUMBER)
        VALUES (?, ?, ?, ?, ?)
        """;
    final var productProviderContactQuery = """
        INSERT INTO PRODUCT_PROVIDERS_CONTACTS (PROVIDER_ID, CONTACT_PHONE, CONTACT_EXTRA_PHONE,
                                                CONTACT_EMAIL, CONTACT_SITE)
        VALUES (?, ?, ?, ?, ?)
        """;
    try (var connection = DatabaseManager.getConnection()) {
      connection.setAutoCommit(false);
      try {
        final var providerId = insertRecord(connection, productProviderQuery, instance.getName());
        final var address = instance.getAddress();
        final var addressId = insertRecord(connection, productProviderAddressQuery, providerId,
            address.getRegion(), address.getTown(), address.getStreet(), address.getNumber());
        final var contact = instance.getBusinessContact();
        final var contactId = insertRecord(connection, productProviderContactQuery, providerId,
            contact.getPhone(), contact.getExtraPhone(), contact.getEmail(), contact.getSite());
        connection.commit();
        contact.setId(contactId);
        address.setId(addressId);
        instance.setId(providerId);
        logger.debug("Created a new product provider with id={}", providerId);
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ProductProvider read(final long id) {
    final var query = """
        SELECT *
        FROM PRODUCT_PROVIDERS
            INNER JOIN PRODUCT_PROVIDERS_CONTACTS
                ON PRODUCT_PROVIDERS.PROVIDER_ID = PRODUCT_PROVIDERS_CONTACTS.PROVIDER_ID
            INNER JOIN PRODUCT_PROVIDERS_ADDRESSES
                ON PRODUCT_PROVIDERS.PROVIDER_ID = PRODUCT_PROVIDERS_ADDRESSES.PROVIDER_ID
        WHERE PRODUCT_PROVIDERS.PROVIDER_ID=?
        """;
    try {
      var result = selectRecord(query, id);
      logger.debug("Selected a product provider with id={}", result.getId());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(final ProductProvider instance) {
    Objects.requireNonNull(instance);
    final var query = """
        UPDATE PRODUCT_PROVIDERS_CONTACTS
        SET CONTACT_PHONE=?,
            CONTACT_EXTRA_PHONE=?,
            CONTACT_EMAIL=?,
            CONTACT_SITE=?
        WHERE CONTACT_ID=?;
        UPDATE PRODUCT_PROVIDERS_ADDRESSES
        SET ADDRESS_REGION=?,
            ADDRESS_TOWN=?,
            ADDRESS_STREET=?,
            ADDRESS_NUMBER=?
        WHERE ADDRESS_ID=?;
        UPDATE PRODUCT_PROVIDERS
        SET PROVIDER_NAME=?
        WHERE PROVIDER_ID=?
        """;
    final var contact = instance.getBusinessContact();
    final var address = instance.getAddress();
    try {
      execute(query, contact.getPhone(), contact.getExtraPhone(), contact.getEmail(),
          contact.getSite(), contact.getId(), address.getRegion(), address.getTown(),
          address.getStreet(), address.getNumber(), address.getId(), instance.getName(),
          instance.getId());
      logger.debug("Updated a product provider with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final ProductProvider instance) {
    try {
      var result = execute("DELETE FROM PRODUCT_PROVIDERS WHERE PROVIDER_ID=?", instance.getId());
      if (result == 0) {
        throw new SQLException("Deleted nothing");
      }
      logger.debug("Deleted a product provider with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected ProductProvider transformToObj(final ResultSet resultSet) throws SQLException {
    var contact = BusinessContact.Builder.create()
        .setId(resultSet.getLong("PRODUCT_PROVIDERS_CONTACTS.CONTACT_ID"))
        .setPhone(resultSet.getString("PRODUCT_PROVIDERS_CONTACTS.CONTACT_PHONE"))
        .setExtraPhone(resultSet.getString("PRODUCT_PROVIDERS_CONTACTS.CONTACT_EXTRA_PHONE"))
        .setEmail(resultSet.getString("PRODUCT_PROVIDERS_CONTACTS.CONTACT_EMAIL"))
        .setSite(resultSet.getString("PRODUCT_PROVIDERS_CONTACTS.CONTACT_SITE"))
        .build();
    var address = Address.Builder.create()
        .setId(resultSet.getLong("PRODUCT_PROVIDERS_ADDRESSES.ADDRESS_ID"))
        .setRegion(resultSet.getString("PRODUCT_PROVIDERS_ADDRESSES.ADDRESS_REGION"))
        .setTown(resultSet.getString("PRODUCT_PROVIDERS_ADDRESSES.ADDRESS_TOWN"))
        .setStreet(resultSet.getString("PRODUCT_PROVIDERS_ADDRESSES.ADDRESS_STREET"))
        .setNumber(resultSet.getString("PRODUCT_PROVIDERS_ADDRESSES.ADDRESS_NUMBER"))
        .build();
    return ProductProvider.Builder.create()
        .setId(resultSet.getLong("PRODUCT_PROVIDERS.PROVIDER_ID"))
        .setName(resultSet.getString("PRODUCT_PROVIDERS.PROVIDER_NAME"))
        .setBusinessContact(contact)
        .setAddress(address)
        .build();
  }
}
