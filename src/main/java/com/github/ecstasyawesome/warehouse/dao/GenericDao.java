package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.util.ConnectionPool;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class GenericDao<T> {

  public abstract List<T> getAll();

  public abstract long create(T instance);

  public abstract T get(long id);

  public abstract void update(T instance);

  public abstract void delete(long id);

  protected final long insertRecord(final String query, final T instance,
      final BiConsumer<PreparedStatement, T> setter) throws SQLException {
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        setter.accept(statement, instance);
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
    }
  }

  protected final void insertRecords(final String query, final List<T> instances,
      final BiConsumer<PreparedStatement, T> setter) throws SQLException {
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(query)) {
        for (T instance : instances) {
          setter.accept(statement, instance);
          statement.addBatch();
        }
        statement.executeBatch();
        connection.commit();
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    }
  }

  protected final T selectRecord(final String query, final Function<ResultSet, T> parser)
      throws SQLException {
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(query);
        var resultSet = statement.executeQuery()) {
      resultSet.first();
      return parser.apply(resultSet);
    }
  }

  protected final List<T> selectRecords(final String query, final Function<ResultSet, T> parser)
      throws SQLException {
    final var result = new ArrayList<T>();
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(query);
        var resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        result.add(parser.apply(resultSet));
      }
    }
    return result;
  }

  protected final void processRecord(final String query) throws SQLException {
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(query)) {
        statement.execute();
        connection.commit();
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    }
  }

  protected final void processRecord(final String query, final T instance,
      final BiConsumer<PreparedStatement, T> setter) throws SQLException {
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(query)) {
        setter.accept(statement, instance);
        statement.execute();
        connection.commit();
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    }
  }

  protected final boolean hasQueryResult(final String query) throws SQLException {
    final var request = String.format("SELECT EXISTS (%s)", query);
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(request);
        var resultSet = statement.executeQuery()) {
      return resultSet.first() && resultSet.getBoolean(1);
    }
  }

  protected final void checkStringParameter(final String parameter) {
    Objects.requireNonNull(parameter);
    if (parameter.isBlank()) {
      throw new IllegalArgumentException();
    }
  }

  protected final NullPointerException createNpeWithSuppressedException(
      final SQLException sqlException) {
    var exception = new NullPointerException();
    exception.addSuppressed(sqlException);
    return exception;
  }
}
