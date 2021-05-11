package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
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
  public boolean isLoginPresent(final String login) {
    checkStringParameter(login);
    final var query = String.format("SELECT * FROM USERS WHERE LOGIN='%s'", login);
    try {
      var result = hasQueryResult(query);
      logger.debug("Login '{}' is present [{}]", login, result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public boolean isTableEmpty() {
    try {
      var result = !hasQueryResult("SELECT * FROM USERS");
      logger.debug("Table is empty [{}]", result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public List<User> getAll() {
    try {
      var result = selectRecords("SELECT * FROM USERS", this::getUserFromResultSet);
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
        INSERT INTO USERS (SURNAME, NAME, SECOND_NAME, PHONE, LOGIN, PASSWORD, ACCESS)
        VALUES (?, ?, ?, ?, ?, ?, ?);
        """;
    try {
      var result = insertRecord(query, instance, this::prepareStatement);
      logger.debug("Created a new user with id={}", result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public User get(final String login) {
    checkStringParameter(login);
    final var query = String.format("SELECT * FROM USERS WHERE LOGIN='%s'", login);
    try {
      var result = selectRecord(query, this::getUserFromResultSet);
      logger.debug("Selected a user with id={} by login '{}'", result.getId(), login);
      return selectRecord(query, this::getUserFromResultSet);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public User get(final long id) {
    final var query = String.format("SELECT * FROM USERS WHERE ID=%d", id);
    try {
      var result = selectRecord(query, this::getUserFromResultSet);
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
        SET SURNAME=?, NAME=?, SECOND_NAME=?, PHONE=?, LOGIN=?, PASSWORD=?, ACCESS=?
        WHERE ID=%d
        """, instance.getId());
    try {
      processRecord(query, instance, this::prepareStatement);
      logger.debug("Updated a user with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final long id) {
    final var query = String.format("DELETE FROM USERS WHERE ID=%d", id);
    try {
      processRecord(query);
      logger.debug("Deleted a user with id={}", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  private void prepareStatement(PreparedStatement statement, User instance) {
    try {
      statement.setString(1, instance.getSurname());
      statement.setString(2, instance.getName());
      statement.setString(3, instance.getSecondName());
      statement.setString(4, instance.getPhone());
      statement.setString(5, instance.getLogin());
      statement.setString(6, instance.getPassword());
      statement.setString(7, instance.getAccess().name());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  private User getUserFromResultSet(ResultSet resultSet) {
    try {
      return User.builder()
          .id(resultSet.getLong("ID"))
          .surname(resultSet.getString("SURNAME"))
          .name(resultSet.getString("NAME"))
          .secondName(resultSet.getString("SECOND_NAME"))
          .phone(resultSet.getString("PHONE"))
          .login(resultSet.getString("LOGIN"))
          .password(resultSet.getString("PASSWORD"))
          .access(Access.valueOf(resultSet.getString("ACCESS")))
          .build();
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }
}
