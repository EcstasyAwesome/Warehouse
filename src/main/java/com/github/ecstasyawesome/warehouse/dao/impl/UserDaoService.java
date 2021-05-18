package com.github.ecstasyawesome.warehouse.dao.impl;

import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.dao.UserDao;
import com.github.ecstasyawesome.warehouse.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
    final var query = String.format("SELECT 1 FROM USERS WHERE USER_LOGIN='%s'", login);
    try {
      var result = !hasQueryResult(query);
      logger.debug("Login '{}' is unique [{}]", login, result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public boolean hasTableRecords() {
    try {
      var result = hasQueryResult("SELECT 1 FROM USERS LIMIT 1");
      logger.debug("Table has records [{}]", result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<User> getAll() {
    try {
      var result = selectRecords("SELECT * FROM USERS");
      logger.debug("Selected all users [{} records]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public long create(final User instance) {
    Objects.requireNonNull(instance);
    final var query = """
        INSERT INTO USERS (USER_SURNAME, USER_NAME, USER_SECOND_NAME, USER_PHONE, USER_LOGIN,
                           USER_PASSWORD, USER_ACCESS)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
    try {
      var result = insertRecord(query, instance);
      logger.debug("Created a new user with id={}", result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public User get(final String login) {
    checkStringParameter(login);
    final var query = String.format("SELECT * FROM USERS WHERE USER_LOGIN='%s'", login);
    try {
      var result = selectRecord(query);
      logger.debug("Selected a user with id={} by login '{}'", result.getId(), login);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public User get(final long id) {
    final var query = String.format("SELECT * FROM USERS WHERE USER_ID=%d", id);
    try {
      var result = selectRecord(query);
      logger.debug("Selected a user with id={}", result.getId());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(final User instance) {
    Objects.requireNonNull(instance);
    final var query = String.format("""
        UPDATE USERS
        SET USER_SURNAME=?,
            USER_NAME=?,
            USER_SECOND_NAME=?,
            USER_PHONE=?,
            USER_LOGIN=?,
            USER_PASSWORD=?,
            USER_ACCESS=?
        WHERE USER_ID=%d
        """, instance.getId());
    try {
      processRecord(query, instance);
      logger.debug("Updated a user with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final long id) {
    final var query = String.format("DELETE FROM USERS WHERE USER_ID=%d", id);
    try {
      processRecord(query);
      logger.debug("Deleted a user with id={}", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected void serialize(final PreparedStatement statement, final User instance)
      throws SQLException {
    statement.setString(1, instance.getSurname());
    statement.setString(2, instance.getName());
    statement.setString(3, instance.getSecondName());
    statement.setString(4, instance.getPhone());
    statement.setString(5, instance.getLogin());
    statement.setString(6, instance.getPassword());
    statement.setString(7, instance.getAccess().name());
  }

  @Override
  protected User deserialize(final ResultSet resultSet) throws SQLException {
    return User.builder()
        .id(resultSet.getLong("USER_ID"))
        .surname(resultSet.getString("USER_SURNAME"))
        .name(resultSet.getString("USER_NAME"))
        .secondName(resultSet.getString("USER_SECOND_NAME"))
        .phone(resultSet.getString("USER_PHONE"))
        .login(resultSet.getString("USER_LOGIN"))
        .password(resultSet.getString("USER_PASSWORD"))
        .access(Access.valueOf(resultSet.getString("USER_ACCESS")))
        .build();
  }
}
