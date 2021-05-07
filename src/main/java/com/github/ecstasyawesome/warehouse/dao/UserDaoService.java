package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.util.ConnectionPool;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
  public boolean isLoginExist(String login) {
    final var query = String.format("SELECT * FROM USERS WHERE LOGIN='%s'", login);
    try {
      return hasQueryResult(query);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public boolean isEmptyTable() {
    try {
      return !hasQueryResult("SELECT * FROM USERS");
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public List<User> getAll() {
    try {
      return getAllByTable("USERS", this::getUserFromResultSet);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public long create(User instance) {
    final var query = """
        INSERT INTO USERS (SURNAME, NAME, SECOND_NAME, PHONE, LOGIN, PASSWORD, ACCESS)
        VALUES (?, ?, ?, ?, ?, ?, ?);
        """;
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        prepareStatement(statement, instance);
        statement.executeUpdate();
        try (var resultSet = statement.getGeneratedKeys()) {
          resultSet.first();
          var id = resultSet.getLong(1);
          connection.commit();
          return id;
        }
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public User get(String login) {
    final var query = "SELECT * FROM USERS WHERE LOGIN=?";
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(query)) {
      statement.setString(1, login);
      try (var resultSet = statement.executeQuery()) {
        resultSet.first();
        return getUserFromResultSet(resultSet);
      }
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public User get(long id) {
    try {
      return getById("USERS", "ID", id, this::getUserFromResultSet);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(User instance) {
    final var query = """
        UPDATE USERS
        SET SURNAME=?, NAME=?, SECOND_NAME=?, PHONE=?, LOGIN=?, PASSWORD=?, ACCESS=?
        WHERE ID=?
        """;
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(query)) {
        prepareStatement(statement, instance);
        statement.setLong(8, instance.getId());
        statement.execute();
        connection.commit();
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(long id) {
    try {
      deleteById("USERS", "ID", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  private void prepareStatement(PreparedStatement statement, User instance) throws SQLException {
    statement.setString(1, instance.getSurname());
    statement.setString(2, instance.getName());
    statement.setString(3, instance.getSecondName());
    statement.setString(4, instance.getPhone());
    statement.setString(5, instance.getLogin());
    statement.setString(6, instance.getPassword());
    statement.setString(7, instance.getAccess().name());
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
