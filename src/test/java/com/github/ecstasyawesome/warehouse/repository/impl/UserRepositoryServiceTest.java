package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class UserRepositoryServiceTest {

  private final UserRepository userRepository = UserRepositoryService.getInstance();
  private MockedStatic<DatabaseManager> mockedDatabase;

  @BeforeEach
  public void setUp() throws SQLException {
    clearDatabase();
    mockedDatabase = mockStatic(DatabaseManager.class);
    mockedDatabase.when(DatabaseManager::getConnection)
        .then(answer -> TestDatabase.getConnection());
  }

  @SuppressWarnings("SqlWithoutWhere")
  private void clearDatabase() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.addBatch("DELETE FROM USERS");
      statement.addBatch("DELETE FROM USERS_CONTACTS");
      statement.addBatch("DELETE FROM USERS_SECURITY");
      statement.addBatch("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1");
      statement.addBatch("ALTER TABLE USERS_CONTACTS ALTER COLUMN CONTACT_ID RESTART WITH 1");
      statement.addBatch("ALTER TABLE USERS_SECURITY ALTER COLUMN SECURITY_ID RESTART WITH 1");
      statement.executeBatch();
    }
  }

  @AfterEach
  public void tearDown() {
    mockedDatabase.close();
  }

  @Test
  public void testSelectByLogin() {
    var user = createUser("login");
    userRepository.create(user);
    assertEquals(user, userRepository.select(user.getPersonSecurity().getLogin()));
  }

  @Test
  public void testSelectByLoginAbsentRecord() {
    assertThrows(NullPointerException.class, () -> userRepository.select("login"));
  }

  @Test
  public void testHasTableRecords() {
    assertFalse(userRepository.hasTableRecords());
    userRepository.create(createUser("login"));
    assertTrue(userRepository.hasTableRecords());
  }

  @Test
  public void testGetAll() {
    var user1 = createUser("login1");
    userRepository.create(user1);
    var user2 = createUser("login2");
    userRepository.create(user2);
    var user3 = createUser("login3");
    userRepository.create(user3);
    var expected = List.of(user1, user2, user3);
    var actual = userRepository.getAll();
    assertEquals(expected.size(), actual.size());
    IntStream.range(0, expected.size())
        .forEach(index -> assertEquals(expected.get(index), actual.get(index)));
  }

  @Test
  public void testGetAllEmptyList() {
    var actual = userRepository.getAll();
    assertEquals(0, actual.size());
  }

  @Test
  public void testReadAndCreate() {
    var user = createUser("login");
    userRepository.create(user);
    assertEquals(1, user.getId());
    assertEquals(user, userRepository.read(user.getId()));
  }

  @Test
  public void testReadAbsentEntry() {
    assertThrows(NullPointerException.class, () -> userRepository.read(1));
  }

  @Test
  public void testCreateDuplicate() {
    var user = createUser("login");
    userRepository.create(user);
    assertEquals(1, user.getId());
    assertEquals(user, userRepository.read(user.getId()));
    assertThrows(NullPointerException.class, () -> userRepository.create(user));
  }

  @Test
  public void testUpdate() {
    var user = createUser("login");
    userRepository.create(user);
    user.setSurname("New surname");
    user.setName("New name");
    user.setSecondName("New second name");
    user.getPersonContact().setEmail("new_mail@mail.com");
    user.getPersonContact().setPhone("0958264512");
    user.getPersonSecurity().setPassword("qwerty123");
    user.getPersonSecurity().setLogin("new_login");
    user.getPersonSecurity().setAccess(Access.ADMIN);
    userRepository.update(user);
    assertEquals(user, userRepository.read(user.getId()));
  }

  @Test
  public void testUpdateDuplicate() {
    var user1 = createUser("login1");
    userRepository.create(user1);
    var user2 = createUser("login2");
    userRepository.create(user2);
    user2.getPersonSecurity().setLogin(user1.getPersonSecurity().getLogin());
    assertThrows(NullPointerException.class, () -> userRepository.update(user2));
  }

  @Test
  public void testDelete() {
    var user = createUser("login");
    userRepository.create(user);
    assertEquals(1, user.getId());
    userRepository.delete(user);
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM USERS_SECURITY)"));
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM USERS_CONTACTS)"));
      try (var resultSet = statement.getResultSet()) {
        while (resultSet.next()) {
          assertFalse(resultSet.getBoolean(1));
        }
      }
    } catch (SQLException exception) {
      fail(exception);
    }
    assertThrows(NullPointerException.class, () -> userRepository.read(user.getId()));
  }

  @Test
  public void testDeleteAbsentEntry() {
    var user = createUser("login");
    user.setId(7);
    assertThrows(NullPointerException.class, () -> userRepository.delete(user));
  }
}