package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.AbstractRecord;
import com.github.ecstasyawesome.warehouse.util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class AbstractRepository<T extends AbstractRecord> {

  protected abstract T transformToObj(ResultSet resultSet) throws SQLException;

  protected final long insertRecord(final Connection connection, final String query,
      final Object... values) throws SQLException {
    try (var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      configureStatement(statement, values);
      statement.execute();
      try (var resultSet = statement.getGeneratedKeys()) {
        resultSet.first();
        return resultSet.getLong(1);
      }
    }
  }

  protected final long insertRecord(final String query, final Object... values)
      throws SQLException {
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        configureStatement(statement, values);
        statement.execute();
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

  protected final T selectRecord(final String query, final Object... values) throws SQLException {
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(query)) {
      configureStatement(statement, values);
      statement.execute();
      try (var resultSet = statement.getResultSet()) {
        resultSet.first();
        return transformToObj(resultSet);
      }
    }
  }

  protected final ObservableList<T> selectRecords(final String query, final Object... values)
      throws SQLException {
    final var result = FXCollections.<T>observableArrayList();
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(query)) {
      configureStatement(statement, values);
      statement.execute();
      try (var resultSet = statement.getResultSet()) {
        while (resultSet.next()) {
          result.add(transformToObj(resultSet));
        }
      }
    }
    return result;
  }

  protected final int execute(final String query, final Object... values) throws SQLException {
    try (var connection = ConnectionPool.getConnection()) {
      connection.setAutoCommit(false);
      try (var statement = connection.prepareStatement(query)) {
        configureStatement(statement, values);
        var result = statement.executeUpdate();
        connection.commit();
        return result;
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    }
  }

  protected final boolean check(final String query, final Object... values) throws SQLException {
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(query)) {
      configureStatement(statement, values);
      statement.execute();
      try (var resultSet = statement.getResultSet()) {
        return resultSet.first() && resultSet.getBoolean(1);
      }
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

  private void configureStatement(PreparedStatement statement, Object... values)
      throws SQLException {
    var index = 1;
    for (var value : values) {
      statement.setObject(index++, value);
    }
  }
}
