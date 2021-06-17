package com.github.ecstasyawesome.warehouse.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.IntStream;
import javafx.collections.ObservableList;
import org.h2.jdbc.JdbcSQLNonTransientException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractRepositoryTest extends AbstractRepository<TestModel> {

  private static final int TOTAL_RECORDS = 3;
  private static final String CREATE_TABLE_QUERY = """
      CREATE TABLE IF NOT EXISTS TEST_MODELS
      (
          TEST_MODEL_ID   BIGINT      NOT NULL AUTO_INCREMENT,
          TEST_MODEL_NAME VARCHAR(50) NOT NULL,
          TEST_MODEL_SURNAME VARCHAR(50) NOT NULL,
          CONSTRAINT PK_TEST_MODEL_ID PRIMARY KEY (TEST_MODEL_ID)
      )
      """;
  private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS TEST_MODELS";
  private static final String DELETE_QUERY = "DELETE FROM TEST_MODELS WHERE TEST_MODEL_ID=?";
  private static final String CHECK_QUERY =
      "SELECT EXISTS(SELECT 1 FROM TEST_MODELS WHERE TEST_MODEL_ID=?)";
  private static final String SELECT_QUERY = "SELECT * FROM TEST_MODELS WHERE TEST_MODEL_ID=?";
  private static final String SELECT_ALL_QUERY = "SELECT * FROM TEST_MODELS";
  private static final String INSERT_QUERY = """
      INSERT INTO TEST_MODELS (TEST_MODEL_NAME, TEST_MODEL_SURNAME)
      VALUES (?, ?)
      """;
  private static final String SELECT_BY_ID_QUERY =
      "SELECT * FROM TEST_MODELS WHERE TEST_MODEL_ID=?";

  @BeforeAll
  public static void beforeAll() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.execute(CREATE_TABLE_QUERY);
    }
  }

  @AfterAll
  public static void afterAll() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.execute(DROP_TABLE_QUERY);
    }
  }

  @BeforeEach
  public void setUp() throws SQLException {
    try (var connection = TestDatabase.getConnection()) {
      try (var preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
        for (var index = 1L; index <= TOTAL_RECORDS; index++) {
          preparedStatement.setString(1, String.format("name %d", index));
          preparedStatement.setString(2, String.format("surname %d", index));
          preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
      }
    }
  }

  @SuppressWarnings("SqlWithoutWhere")
  @AfterEach
  public void tearDown() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.addBatch("DELETE FROM TEST_MODELS");
      statement.addBatch("ALTER TABLE TEST_MODELS ALTER COLUMN TEST_MODEL_ID RESTART WITH 1");
      statement.executeBatch();
    }
  }

  @Override
  protected TestModel transformToObj(ResultSet resultSet) throws SQLException {
    var testModel = new TestModel();
    testModel.setId(resultSet.getLong("TEST_MODELS.TEST_MODEL_ID"));
    testModel.setName(resultSet.getString("TEST_MODELS.TEST_MODEL_NAME"));
    testModel.setSurname(resultSet.getString("TEST_MODELS.TEST_MODEL_SURNAME"));
    return testModel;
  }

  @Test
  public void testInsertRecordWithCustomConnection() {
    var id = TOTAL_RECORDS + 1;
    var name = "name";
    var surname = "surname";
    try (var connection = spy(TestDatabase.getConnection())) {
      assertEquals(id, insertRecord(connection, INSERT_QUERY, name, surname));
      verify(connection, never()).setAutoCommit(anyBoolean());
      verify(connection, never()).commit();
      verify(connection, never()).close();
      try (var statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
        statement.setLong(1, id);
        assertTrue(statement.execute());
        try (var resultSet = statement.getResultSet()) {
          assertTrue(resultSet.first());
          var actual = transformToObj(resultSet);
          var expected = new TestModel(id, name, surname);
          assertEquals(expected, actual);
        }
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void checkInsertRecordWithCustomConnectionThrowsSqlException() {
    try (var connection = spy(TestDatabase.getConnection())) {
      assertThrows(SQLException.class, () -> insertRecord(connection, null, null, null));
      verify(connection, never()).setAutoCommit(anyBoolean());
      verify(connection, never()).commit();
      verify(connection, never()).rollback();
      verify(connection, never()).close();
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void testInsertRecord() {
    var id = TOTAL_RECORDS + 1;
    var name = "name";
    var surname = "surname";
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertEquals(id, insertRecord(INSERT_QUERY, name, surname));
        verify(connection).setAutoCommit(false);
        verify(connection).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }

    try (var connection = TestDatabase.getConnection()) {
      try (var statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
        statement.setLong(1, id);
        assertTrue(statement.execute());
        try (var resultSet = statement.getResultSet()) {
          assertTrue(resultSet.first());
          var actual = transformToObj(resultSet);
          var expected = new TestModel(id, name, surname);
          assertEquals(expected, actual);
        }
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void checkInsertRecordThrowsSqlException() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertThrows(SQLException.class, () -> insertRecord(null));
        verify(connection).setAutoCommit(false);
        verify(connection, never()).commit();
        verify(connection).rollback();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void testExecuteSuccess() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertEquals(1, execute(DELETE_QUERY, TOTAL_RECORDS));
        verify(connection).setAutoCommit(false);
        verify(connection).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }

    try (var connection = TestDatabase.getConnection()) {
      try (var statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
        statement.setLong(1, TOTAL_RECORDS);
        assertTrue(statement.execute());
        try (var resultSet = statement.getResultSet()) {
          assertFalse(resultSet.first());
        }
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void testExecuteFailed() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertEquals(0, execute(DELETE_QUERY, TOTAL_RECORDS + 1));
        verify(connection).setAutoCommit(false);
        verify(connection).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void checkExecuteThrowsSqlException() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertThrows(SQLException.class, () -> execute(null, TOTAL_RECORDS));
        verify(connection).setAutoCommit(false);
        verify(connection, never()).commit();
        verify(connection).rollback();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void testCheckSuccess() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertTrue(check(CHECK_QUERY, TOTAL_RECORDS));
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void testCheckFailed() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertFalse(check(CHECK_QUERY, TOTAL_RECORDS + 1));
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void checkThatCheckThrowsSqlException() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertThrows(SQLException.class, () -> check(null));
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void testSelectRecord() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        final var id = 1L;
        var actual = selectRecord(SELECT_QUERY, id);
        var expected = new TestModel(id, String.format("name %d", id),
            String.format("surname %s", id));
        assertEquals(expected, actual);
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void checkSelectRecordThrowsNoDataException() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertThrows(JdbcSQLNonTransientException.class,
            () -> selectRecord(SELECT_QUERY, TOTAL_RECORDS + 1));
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
      fail(exception);
    }
  }

  @Test
  public void checkSelectRecordThrowsSqlException() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertThrows(SQLException.class, () -> selectRecord(null));
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
      fail(exception);
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testSelectRecords() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        var actual = selectRecords(SELECT_ALL_QUERY);
        var expected = new ArrayList<TestModel>();
        IntStream.range(0, TOTAL_RECORDS).forEach(index -> {
          var id = ++index;
          expected.add(new TestModel(id, String.format("name %d", id),
              String.format("surname %s", id)));
        });
        assertEquals(expected, actual);
        assertTrue(actual instanceof ObservableList);
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testSelectNoRecords() {
    try (var connection = spy(TestDatabase.getConnection());
        var statement = connection.prepareStatement(DELETE_QUERY)) {
      for (var index = 1L; index <= TOTAL_RECORDS; index++) {
        statement.setLong(1, index);
        statement.addBatch();
      }
      statement.executeBatch();
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        var actual = selectRecords(SELECT_ALL_QUERY);
        var expected = new ArrayList<TestModel>();
        assertEquals(expected, actual);
        assertTrue(actual instanceof ObservableList);
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void checkSelectRecordsThrowsSqlException() {
    try (var connection = spy(TestDatabase.getConnection())) {
      try (var staticMock = mockStatic(DatabaseManager.class)) {
        staticMock.when(DatabaseManager::getConnection).thenReturn(connection);
        assertThrows(SQLException.class, () -> selectRecords(null));
        verify(connection, never()).setAutoCommit(anyBoolean());
        verify(connection, never()).commit();
        verify(connection).close();
      }
    } catch (SQLException exception) {
      fail(exception);
    }
  }

  @Test
  public void checkThatCheckStringParameterThrowsExceptions() {
    assertThrows(NullPointerException.class, () -> checkStringParameter(null));
    assertThrows(IllegalArgumentException.class, () -> checkStringParameter(""));
    assertThrows(IllegalArgumentException.class, () -> checkStringParameter(" "));
    assertThrows(IllegalArgumentException.class, () -> checkStringParameter("\n"));
    assertDoesNotThrow(() -> checkStringParameter("legal"));
  }

  @Test
  public void testCreateNpeWithSuppressedException() {
    var exception = new SQLException("Some text");
    var npe = createNpeWithSuppressedException(exception);
    var suppressed = npe.getSuppressed();
    assertNotNull(suppressed);
    assertEquals(1, suppressed.length);
    assertEquals(exception, suppressed[0]);
  }

  @SuppressWarnings("ThrowableNotThrown")
  @Test
  public void checkCreateNpeWithSuppressedExceptionThrowsException() {
    assertThrows(NullPointerException.class, () -> createNpeWithSuppressedException(null));
  }
}