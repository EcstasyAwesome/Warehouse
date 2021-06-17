package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createProductProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.repository.ProductProviderRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ProductProviderRepositoryServiceTest {

  private final ProductProviderRepository productProviderRepository =
      ProductProviderRepositoryService.getInstance();

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
      statement.addBatch("DELETE FROM PRODUCT_PROVIDERS");
      statement.addBatch("DELETE FROM PRODUCT_PROVIDERS_ADDRESSES");
      statement.addBatch("DELETE FROM PRODUCT_PROVIDERS_CONTACTS");
      statement.addBatch("ALTER TABLE PRODUCT_PROVIDERS ALTER COLUMN PROVIDER_ID RESTART WITH 1");
      statement.addBatch("""
          ALTER TABLE PRODUCT_PROVIDERS_ADDRESSES
          ALTER COLUMN ADDRESS_ID RESTART WITH 1
          """);
      statement.addBatch("""
          ALTER TABLE PRODUCT_PROVIDERS_CONTACTS
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
  public void testGetAll() {
    var provider1 = createProductProvider("Name1");
    productProviderRepository.create(provider1);
    var provider2 = createProductProvider("Name2");
    productProviderRepository.create(provider2);
    var provider3 = createProductProvider("Name3");
    productProviderRepository.create(provider3);
    var expected = List.of(provider1, provider2, provider3);
    var actual = productProviderRepository.getAll();
    assertEquals(expected.size(), actual.size());
    IntStream.range(0, expected.size())
        .forEach(index -> assertEquals(expected.get(index), actual.get(index)));
  }

  @Test
  public void testGetAllEmptyList() {
    assertEquals(0, productProviderRepository.getAll().size());
  }

  @Test
  public void testReadAndCreate() {
    var provider = createProductProvider("Name");
    productProviderRepository.create(provider);
    assertEquals(1, provider.getId());
    assertEquals(provider, productProviderRepository.read(provider.getId()));
  }

  @Test
  public void testReadAbsentEntry() {
    assertThrows(NullPointerException.class, () -> productProviderRepository.read(1));
  }

  @Test
  public void testCreateDuplicate() {
    var provider = createProductProvider("Name");
    productProviderRepository.create(provider);
    assertEquals(1, provider.getId());
    assertEquals(provider, productProviderRepository.read(provider.getId()));
    assertThrows(NullPointerException.class, () -> productProviderRepository.create(provider));
  }

  @Test
  public void testUpdate() {
    var provider = createProductProvider("Name");
    productProviderRepository.create(provider);
    provider.setName("New name");
    provider.getBusinessContact().setPhone("0958567845");
    provider.getBusinessContact().setExtraPhone("0958586512");
    provider.getBusinessContact().setEmail("new_email@mail.com");
    provider.getBusinessContact().setSite("new-example.com");
    provider.getAddress().setTown("New town");
    provider.getAddress().setRegion("New region");
    provider.getAddress().setStreet("New street");
    provider.getAddress().setNumber("134/7");
    productProviderRepository.update(provider);
    assertEquals(provider, productProviderRepository.read(provider.getId()));
  }

  @Test
  public void testUpdateDuplicate() {
    var provider1 = createProductProvider("Name1");
    productProviderRepository.create(provider1);
    var provider2 = createProductProvider("Name2");
    productProviderRepository.create(provider2);
    provider2.setName(provider1.getName());
    assertThrows(NullPointerException.class, () -> productProviderRepository.update(provider2));
  }

  @Test
  public void testDelete() {
    var provider = createProductProvider("Name");
    productProviderRepository.create(provider);
    assertEquals(1, provider.getId());
    productProviderRepository.delete(provider);
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM PRODUCT_PROVIDERS_ADDRESSES)"));
      assertTrue(statement.execute("SELECT EXISTS(SELECT 1 FROM PRODUCT_PROVIDERS_CONTACTS)"));
      try (var resultSet = statement.getResultSet()) {
        while (resultSet.next()) {
          assertFalse(resultSet.getBoolean(1));
        }
      }
    } catch (SQLException exception) {
      fail(exception);
    }
    assertThrows(NullPointerException.class,
        () -> productProviderRepository.read(provider.getId()));
  }

  @Test
  public void testDeleteAbsentEntry() {
    var provider = createProductProvider("Name");
    provider.setId(7);
    assertThrows(NullPointerException.class, () -> productProviderRepository.delete(provider));
  }
}