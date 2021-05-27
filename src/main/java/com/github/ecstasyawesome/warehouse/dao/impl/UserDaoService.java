package com.github.ecstasyawesome.warehouse.dao.impl;

import com.github.ecstasyawesome.warehouse.dao.UserDao;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.PersonSecurity;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.model.PersonContact;
import com.github.ecstasyawesome.warehouse.util.ConnectionPool;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDaoService extends UserDao {

  private static final UserDaoService INSTANCE = new UserDaoService();
  private final Logger logger = LogManager.getLogger(UserDaoService.class);

  private UserDaoService() {
  }

  public static UserDaoService getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isFieldUnique(final String login) {
    checkStringParameter(login);
    final var query = "SELECT EXISTS(SELECT 1 FROM USERS_SECURITY WHERE USER_SECURITY_LOGIN=?)";
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
                 INNER JOIN USERS_CONTACTS
                            ON USERS.USER_ID = USERS_CONTACTS.USER_ID
                 INNER JOIN USERS_SECURITY
                            ON USERS.USER_ID = USERS_SECURITY.USER_ID
        """;
    try {
      var result = selectRecords(query);
      logger.debug("Selected all users [{} records]", result.size());
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
        INSERT INTO USERS_CONTACTS (USER_ID, USER_CONTACT_PHONE, USER_CONTACT_EMAIL)
        VALUES (?, ?, ?)
        """;
    final var userSecurityQuery = """
        INSERT INTO USERS_SECURITY (USER_ID, USER_SECURITY_LOGIN, USER_SECURITY_PASSWORD,
                                    USER_SECURITY_ACCESS)
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
        final var securityId = insertRecord(connection, userSecurityQuery, userId,
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
                 INNER JOIN USERS_CONTACTS
                            ON USERS.USER_ID = USERS_CONTACTS.USER_ID
                 INNER JOIN USERS_SECURITY
                            ON USERS.USER_ID = USERS_SECURITY.USER_ID
        WHERE USER_SECURITY_LOGIN=?
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
                 INNER JOIN USERS_CONTACTS
                            ON USERS.USER_ID = USERS_CONTACTS.USER_ID
                 INNER JOIN USERS_SECURITY
                            ON USERS.USER_ID = USERS_SECURITY.USER_ID
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
        UPDATE USERS_CONTACTS
        SET USER_CONTACT_PHONE=?,
            USER_CONTACT_EMAIL=?
        WHERE USER_CONTACT_ID=?;
        UPDATE USERS_SECURITY
        SET USER_SECURITY_LOGIN=?,
            USER_SECURITY_PASSWORD=?,
            USER_SECURITY_ACCESS=?
        WHERE USER_SECURITY_ID=?;
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
        .setId(resultSet.getLong("USER_CONTACT_ID"))
        .setPhone(resultSet.getString("USER_CONTACT_PHONE"))
        .setEmail(resultSet.getString("USER_CONTACT_EMAIL"))
        .build();
    var security = PersonSecurity.Builder.create()
        .setId(resultSet.getLong("USER_SECURITY_ID"))
        .setLogin(resultSet.getString("USER_SECURITY_LOGIN"))
        .setPassword(resultSet.getString("USER_SECURITY_PASSWORD"))
        .setAccess(Access.valueOf(resultSet.getString("USER_SECURITY_ACCESS")))
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
