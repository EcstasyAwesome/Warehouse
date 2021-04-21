package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.util.ConnectionPool;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class GenericDao<T> {

  public abstract List<T> getAll();

  public abstract long create(T instance);

  public abstract T get(long id);

  public abstract void update(T instance);

  public abstract void delete(long id);

  protected final List<T> getAllByTable(final String table, Function<ResultSet, T> parser)
      throws SQLException {
    final var result = new ArrayList<T>();
    final var query = String.format("SELECT * FROM %s", table);
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(query);
        var resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        result.add(parser.apply(resultSet));
      }
    }
    return result;
  }

  protected final T getById(final String table, final String column, final long id,
      Function<ResultSet, T> parser) throws SQLException {
    final var query = String.format("SELECT * FROM %s WHERE %s=%d", table, column, id);
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(query);
        var resultSet = statement.executeQuery()) {
      if (resultSet.next()) {
        return parser.apply(resultSet);
      }
    }
    throw new NullPointerException();
  }

  protected final void deleteById(final String table, final String column, final long id)
      throws SQLException {
    final var query = String.format("DELETE FROM %s WHERE %s=%d", table, column, id);
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

  protected final boolean hasQueryResult(final String query) throws SQLException {
    final var request = String.format("SELECT EXISTS (%s)", query);
    try (var connection = ConnectionPool.getConnection();
        var statement = connection.prepareStatement(request);
        var resultSet = statement.executeQuery()) {
      return resultSet.first() && resultSet.getBoolean(1);
    }
  }
}
