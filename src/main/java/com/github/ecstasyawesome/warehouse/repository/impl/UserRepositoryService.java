package com.github.ecstasyawesome.warehouse.repository.impl;

import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.PersonContact;
import com.github.ecstasyawesome.warehouse.model.PersonSecurity;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.util.ConnectionPool;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRepositoryService extends UserRepository {

  private static final UserRepositoryService INSTANCE = new UserRepositoryService();
  private final Logger logger = LogManager.getLogger(UserRepositoryService.class);

  private UserRepositoryService() {
  }

  public static UserRepositoryService getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isFieldUnique(final String login) {
    checkStringParameter(login);
    final var query = "SELECT EXISTS(SELECT 1 FROM SECURITY WHERE SECURITY_LOGIN=?)";
    try {
      var result = !check(query, login);
      logger.debug("Login '{}' is unique [{}]", login, result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public boolean hasTableRecords() {
    try {
      var result = check("SELECT EXISTS(SELECT 1 FROM USERS LIMIT 1)");
      logger.debug("Table has records [{}]", result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<User> getAll() {
    final var query = """
        SELECT *
        FROM USERS
                 INNER JOIN PERSONS_CONTACTS
                            ON USERS.USER_ID = PERSONS_CONTACTS.USER_ID
                 INNER JOIN SECURITY
                            ON USERS.USER_ID = SECURITY.USER_ID
        """;
    try {
      var result = selectRecords(query);
      logger.debug("Selected all users [{} record(s)]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void create(final User instance) {
    Objects.requireNonNull(instance);
    final var userQuery = """
        INSERT INTO USERS (USER_SURNAME, USER_NAME, USER_SECOND_NAME)
        VALUES (?, ?, ?)
        """;
    final var userContactQuery = """
        INSERT INTO PERSONS_CONTACTS (USER_ID, PERSON_CONTACT_PHONE, PERSON_CONTACT_EMAIL)
        VALUES (?, ?, ?)
        """;
    final var securityQuery = """
        INSERT INTO SECURITY (USER_ID, SECURITY_LOGIN, SECURITY_PASSWORD, SECURITY_ACCESS)
        VALUES (?, ?, ?, ?)
        """;
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try {
        final var userId = insertRecord(connection, userQuery, instance.getSurname(),
            instance.getName(), instance.getSecondName());
        final var contact = instance.getPersonContact();
        final var contactId = insertRecord(connection, userContactQuery, userId, contact.getPhone(),
            contact.getEmail());
        final var security = instance.getPersonSecurity();
        final var securityId = insertRecord(connection, securityQuery, userId,
            security.getLogin(), security.getPassword(), security.getAccess().name());
        connection.commit();
        contact.setId(contactId);
        security.setId(securityId);
        instance.setId(userId);
        logger.debug("Created a new user with id={}", userId);
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public User get(final String login) {
    checkStringParameter(login);
    final var query = """
        SELECT *
        FROM USERS
                 INNER JOIN PERSONS_CONTACTS
                            ON USERS.USER_ID = PERSONS_CONTACTS.USER_ID
                 INNER JOIN SECURITY
                            ON USERS.USER_ID = SECURITY.USER_ID
        WHERE SECURITY_LOGIN=?
        """;
    try {
      var result = selectRecord(query, login);
      logger.debug("Selected a user with id={} by login '{}'", result.getId(), login);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public User get(final long id) {
    final var query = """
        SELECT *
        FROM USERS
                 INNER JOIN PERSONS_CONTACTS
                            ON USERS.USER_ID = PERSONS_CONTACTS.USER_ID
                 INNER JOIN SECURITY
                            ON USERS.USER_ID = SECURITY.USER_ID
        WHERE USERS.USER_ID=?
        """;
    try {
      var result = selectRecord(query, id);
      logger.debug("Selected a user with id={}", result.getId());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(final User instance) {
    Objects.requireNonNull(instance);
    final var query = """
        UPDATE PERSONS_CONTACTS
        SET PERSON_CONTACT_PHONE=?,
            PERSON_CONTACT_EMAIL=?
        WHERE PERSON_CONTACT_ID=?;
        UPDATE SECURITY
        SET SECURITY_LOGIN=?,
            SECURITY_PASSWORD=?,
            SECURITY_ACCESS=?
        WHERE SECURITY_ID=?;
        UPDATE USERS
        SET USER_SURNAME=?,
            USER_NAME=?,
            USER_SECOND_NAME=?
        WHERE USER_ID=?
        """;
    final var contact = instance.getPersonContact();
    final var security = instance.getPersonSecurity();
    try {
      execute(query, contact.getPhone(), contact.getEmail(), contact.getId(), security.getLogin(),
          security.getPassword(), security.getAccess().name(), security.getId(),
          instance.getSurname(), instance.getName(), instance.getSecondName(), instance.getId());
      logger.debug("Updated a user with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final long id) {
    try {
      var result = execute("DELETE FROM USERS WHERE USER_ID=?", id);
      if (result == 0) {
        throw new SQLException("Deleted nothing");
      }
      logger.debug("Deleted a user with id={}", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected User transformToObj(final ResultSet resultSet) throws SQLException {
    var contact = PersonContact.Builder.create()
        .setId(resultSet.getLong("PERSON_CONTACT_ID"))
        .setPhone(resultSet.getString("PERSON_CONTACT_PHONE"))
        .setEmail(resultSet.getString("PERSON_CONTACT_EMAIL"))
        .build();
    var security = PersonSecurity.Builder.create()
        .setId(resultSet.getLong("SECURITY_ID"))
        .setLogin(resultSet.getString("SECURITY_LOGIN"))
        .setPassword(resultSet.getString("SECURITY_PASSWORD"))
        .setAccess(Access.valueOf(resultSet.getString("SECURITY_ACCESS")))
        .build();
    return User.Builder.create()
        .setId(resultSet.getLong("USER_ID"))
        .setSurname(resultSet.getString("USER_SURNAME"))
        .setName(resultSet.getString("USER_NAME"))
        .setSecondName(resultSet.getString("USER_SECOND_NAME"))
        .setUserContact(contact)
        .setUserSecurity(security)
        .build();
  }
}
