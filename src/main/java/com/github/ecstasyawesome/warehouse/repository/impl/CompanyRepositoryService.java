package com.github.ecstasyawesome.warehouse.repository.impl;

import com.github.ecstasyawesome.warehouse.model.Address;
import com.github.ecstasyawesome.warehouse.model.BusinessContact;
import com.github.ecstasyawesome.warehouse.model.Company;
import com.github.ecstasyawesome.warehouse.model.PersonType;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompanyRepositoryService extends CompanyRepository {

  private static final CompanyRepositoryService INSTANCE = new CompanyRepositoryService();
  private final Logger logger = LogManager.getLogger(CompanyRepositoryService.class);

  private CompanyRepositoryService() {
  }

  public static CompanyRepositoryService getInstance() {
    return INSTANCE;
  }

  @Override
  public ObservableList<Company> getAll() {
    final var query = """
        SELECT *
        FROM COMPANIES
            INNER JOIN COMPANIES_CONTACTS
                ON COMPANIES.COMPANY_ID = COMPANIES_CONTACTS.COMPANY_ID
            INNER JOIN COMPANIES_ADDRESSES
                ON COMPANIES.COMPANY_ID = COMPANIES_ADDRESSES.COMPANY_ID
        """;
    try {
      var result = selectRecords(query);
      logger.debug("Selected all companies [{} record(s)]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void create(Company instance) {
    Objects.requireNonNull(instance);
    final var companyQuery = """
        INSERT INTO COMPANIES (COMPANY_NAME, COMPANY_IDENTIFIER_CODE, COMPANY_PERSON_TYPE)
        VALUES (?, ?, ?)
        """;
    final var companyContactQuery = """
        INSERT INTO COMPANIES_CONTACTS (COMPANY_ID, CONTACT_PHONE, CONTACT_EXTRA_PHONE,
                                        CONTACT_EMAIL, CONTACT_SITE)
        VALUES (?, ?, ?, ?, ?)
        """;
    final var companyAddressQuery = """
        INSERT INTO COMPANIES_ADDRESSES (COMPANY_ID, ADDRESS_REGION, ADDRESS_TOWN, ADDRESS_STREET,
                                         ADDRESS_NUMBER)
        VALUES (?, ?, ?, ?, ?)
        """;
    try (var connection = DatabaseManager.getConnection()) {
      connection.setAutoCommit(false);
      try {
        final var companyId = insertRecord(connection, companyQuery, instance.getName(),
            instance.getIdentifierCode(), instance.getPersonType().name());
        final var contact = instance.getBusinessContact();
        final var contactId = insertRecord(connection, companyContactQuery, companyId,
            contact.getPhone(), contact.getExtraPhone(), contact.getEmail(), contact.getSite());
        final var address = instance.getAddress();
        final var addressId = insertRecord(connection, companyAddressQuery, companyId,
            address.getRegion(), address.getTown(), address.getStreet(), address.getNumber());
        connection.commit();
        contact.setId(contactId);
        address.setId(addressId);
        instance.setId(companyId);
        logger.debug("Created a new company with id={}", companyId);
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public Company get(long id) {
    final var query = """
        SELECT *
        FROM COMPANIES
            INNER JOIN COMPANIES_CONTACTS
                ON COMPANIES.COMPANY_ID = COMPANIES_CONTACTS.COMPANY_ID
            INNER JOIN COMPANIES_ADDRESSES
                ON COMPANIES.COMPANY_ID = COMPANIES_ADDRESSES.COMPANY_ID
        WHERE COMPANIES.COMPANY_ID=?
        """;
    try {
      var result = selectRecord(query, id);
      logger.debug("Selected a company with id={}", result.getId());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(Company instance) {
    Objects.requireNonNull(instance);
    final var query = """
        UPDATE COMPANIES_CONTACTS
        SET CONTACT_PHONE=?,
            CONTACT_EXTRA_PHONE=?,
            CONTACT_EMAIL=?,
            CONTACT_SITE=?
        WHERE CONTACT_ID=?;
        UPDATE COMPANIES_ADDRESSES
        SET ADDRESS_REGION=?,
            ADDRESS_TOWN=?,
            ADDRESS_STREET=?,
            ADDRESS_NUMBER=?
        WHERE ADDRESS_ID=?;
        UPDATE COMPANIES
        SET COMPANY_NAME=?,
            COMPANY_IDENTIFIER_CODE=?,
            COMPANY_PERSON_TYPE=?
        WHERE COMPANY_ID=?
        """;
    final var contact = instance.getBusinessContact();
    final var address = instance.getAddress();
    try {
      execute(query, contact.getPhone(), contact.getExtraPhone(), contact.getEmail(),
          contact.getSite(), contact.getId(), address.getRegion(), address.getTown(),
          address.getStreet(), address.getNumber(), address.getId(), instance.getName(),
          instance.getIdentifierCode(), instance.getPersonType().name(), instance.getId());
      logger.debug("Updated a company with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(long id) {
    try {
      var result = execute("DELETE FROM COMPANIES WHERE COMPANY_ID=?", id);
      if (result == 0) {
        throw new SQLException("Deleted nothing");
      }
      logger.debug("Deleted a company with id={}", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected Company transformToObj(ResultSet resultSet) throws SQLException {
    var contact = BusinessContact.Builder.create()
        .setId(resultSet.getLong("COMPANIES_CONTACTS.CONTACT_ID"))
        .setPhone(resultSet.getString("COMPANIES_CONTACTS.CONTACT_PHONE"))
        .setExtraPhone(resultSet.getString("COMPANIES_CONTACTS.CONTACT_EXTRA_PHONE"))
        .setEmail(resultSet.getString("COMPANIES_CONTACTS.CONTACT_EMAIL"))
        .setSite(resultSet.getString("COMPANIES_CONTACTS.CONTACT_SITE"))
        .build();
    var address = Address.Builder.create()
        .setId(resultSet.getLong("COMPANIES_ADDRESSES.ADDRESS_ID"))
        .setRegion(resultSet.getString("COMPANIES_ADDRESSES.ADDRESS_REGION"))
        .setTown(resultSet.getString("COMPANIES_ADDRESSES.ADDRESS_TOWN"))
        .setStreet(resultSet.getString("COMPANIES_ADDRESSES.ADDRESS_STREET"))
        .setNumber(resultSet.getString("COMPANIES_ADDRESSES.ADDRESS_NUMBER"))
        .build();
    return Company.Builder.create()
        .setId(resultSet.getLong("COMPANIES.COMPANY_ID"))
        .setName(resultSet.getString("COMPANIES.COMPANY_NAME"))
        .setIdentifierCode(resultSet.getString("COMPANIES.COMPANY_IDENTIFIER_CODE"))
        .setPersonType(PersonType.valueOf(resultSet.getString("COMPANIES.COMPANY_PERSON_TYPE")))
        .setBusinessContact(contact)
        .setAddress(address)
        .build();
  }
}
