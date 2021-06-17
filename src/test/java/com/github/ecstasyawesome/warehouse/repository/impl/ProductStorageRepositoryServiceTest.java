package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createProductStorage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.repository.ProductStorageRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ProductStorageRepositoryServiceTest {

  private final ProductStorageRepository productStorageRepository =
      ProductStorageRepositoryService.getInstance();
  private Company company;
  private MockedStatic<DatabaseManager> mockedDatabase;

  @BeforeEach
  public void setUp() throws SQLException {
    clearDatabase();
    mockedDatabase = mockStatic(DatabaseManager.class);
    mockedDatabase.when(DatabaseManager::getConnection)
        .then(answer -> TestDatabase.getConnection());
    company = createCompany("Name", "777777777777777");
    CompanyRepositoryService.getInstance().create(company);
  }

  @SuppressWarnings("SqlWithoutWhere")
  private void clearDatabase() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.addBatch("DELETE FROM COMPANIES");
      statement.addBatch("DELETE FROM PRODUCT_STORAGES");
      statement.addBatch("DELETE FROM PRODUCT_STORAGES_ADDRESSES");
      statement.addBatch("DELETE FROM PRODUCT_STORAGES_CONTACTS");
      statement.addBatch("ALTER TABLE PRODUCT_STORAGES ALTER COLUMN STORAGE_ID RESTART WITH 1");
      statement.addBatch("""
          ALTER TABLE PRODUCT_STORAGES_ADDRESSES
          ALTER COLUMN ADDRESS_ID RESTART WITH 1
          """);
      statement.addBatch("""
          ALTER TABLE PRODUCT_STORAGES_CONTACTS
          ALTER COLUMN CONTACT_ID RESTART WITH 1
          """);
      statement.executeBatch();
    }
  }

  @AfterEach
  public void tearDown() {
    mockedDatabase.close();
  }

  @Test
  public void getAll() {
  }

  @Test
  public void testGetAll() {
    var storage1 = createProductStorage("Name1", company);
    productStorageRepository.create(storage1);
    var storage2 = createProductStorage("Name2", company);
    productStorageRepository.create(storage2);
    var storage3 = createProductStorage("Name3", company);
    productStorageRepository.create(storage3);
    var expected = List.of(storage1, storage2, storage3);
    var actual = productStorageRepository.getAll();
    assertEquals(expected.size(), actual.size());
    IntStream.range(0, expected.size())
        .forEach(index -> assertEquals(expected.get(index), actual.get(index)));
  }

  @Test
  public void testGetAllEmptyList() {
    var actual = productStorageRepository.getAll();
    assertEquals(0, actual.size());
  }

  @Test
  public void testReadAndCreate() {
    var storage = createProductStorage("Name", company);
    productStorageRepository.create(storage);
    assertEquals(1, storage.getId());
    assertEquals(storage, productStorageRepository.read(storage.getId()));
  }

  @Test
  public void testReadAbsentEntry() {
    assertThrows(NullPointerException.class, () -> productStorageRepository.read(1));
  }

  @Test
  public void testCreateDuplicate() {
    var storage = createProductStorage("Name", company);
    productStorageRepository.create(storage);
    assertEquals(1, storage.getId());
    assertEquals(storage, productStorageRepository.read(storage.getId()));
    assertThrows(NullPointerException.class, () -> productStorageRepository.create(storage));
  }

  @Test
  public void testUpdate() {
    var storage = createProductStorage("Name", company);
    productStorageRepository.create(storage);
    storage.setName("New name");
    productStorageRepository.update(storage);
    assertEquals(storage, productStorageRepository.read(storage.getId()));
  }

  @Test
  public void testUpdateDuplicate() {
    var storage1 = createProductStorage("Name1", company);
    productStorageRepository.create(storage1);
    var storage2 = createProductStorage("Name2", company);
    productStorageRepository.create(storage2);
    storage2.setName(storage1.getName());
    assertThrows(NullPointerException.class, () -> productStorageRepository.update(storage2));
  }

  @Test
  public void testDelete() {
    var storage = createProductStorage("Name", company);
    productStorageRepository.create(storage);
    assertEquals(1, storage.getId());
    productStorageRepository.delete(storage);
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM PRODUCT_STORAGES_ADDRESSES)"));
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM PRODUCT_STORAGES_CONTACTS)"));
      try (var resultSet = statement.getResultSet()) {
        while (resultSet.next()) {
          assertFalse(resultSet.getBoolean(1));
        }
      }
    } catch (SQLException exception) {
      fail(exception);
    }
    assertThrows(NullPointerException.class,
        () -> productStorageRepository.read(storage.getId()));
  }

  @Test
  public void testDeleteAbsentEntry() {
    var storage = createProductStorage("Name", company);
    storage.setId(7);
    assertThrows(NullPointerException.class, () -> productStorageRepository.delete(storage));
  }

  @Test
  public void checkCascadeDeletingByCompany() {
    var storage = createProductStorage("Name", company);
    productStorageRepository.create(storage);
    assertEquals(storage, productStorageRepository.read(storage.getId()));
    CompanyRepositoryService.getInstance().delete(company);
    assertThrows(NullPointerException.class, () -> productStorageRepository.read(storage.getId()));
  }
}