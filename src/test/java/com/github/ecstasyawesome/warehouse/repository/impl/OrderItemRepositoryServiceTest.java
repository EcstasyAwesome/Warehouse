package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.repository.AbstractTestEntryRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.repository.AbstractTestEntryRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.repository.AbstractTestEntryRepository.createOrder;
import static com.github.ecstasyawesome.warehouse.repository.AbstractTestEntryRepository.createOrderItems;
import static com.github.ecstasyawesome.warehouse.repository.AbstractTestEntryRepository.createProduct;
import static com.github.ecstasyawesome.warehouse.repository.AbstractTestEntryRepository.createProductProvider;
import static com.github.ecstasyawesome.warehouse.repository.AbstractTestEntryRepository.createProductStorage;
import static com.github.ecstasyawesome.warehouse.repository.AbstractTestEntryRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.repository.OrderItemRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class OrderItemRepositoryServiceTest {

  private static boolean initialized = false;
  private static User user;
  private static ProductProvider provider;
  private static ProductStorage storage;
  private static Product product1;
  private static Product product2;
  private final OrderItemRepository orderItemRepository = OrderItemRepositoryService.getInstance();
  private MockedStatic<DatabaseManager> mockedDatabase;

  @BeforeEach
  public void setUp() throws SQLException {
    clearDatabase();
    mockedDatabase = mockStatic(DatabaseManager.class);
    mockedDatabase.when(DatabaseManager::getConnection)
        .then(answer -> TestDatabase.getConnection());
    if (!initialized) {
      initDatabaseEntries();
      initialized = true;
    }
  }

  @SuppressWarnings("SqlWithoutWhere")
  private void clearDatabase() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.addBatch("DELETE FROM ORDERS");
      statement.addBatch("DELETE FROM ORDERS_ITEMS");
      statement.addBatch("ALTER TABLE ORDERS ALTER COLUMN ORDER_ID RESTART WITH 1");
      statement.addBatch("ALTER TABLE ORDERS_ITEMS ALTER COLUMN ITEM_ID RESTART WITH 1");
      statement.executeBatch();
    }
  }

  private void initDatabaseEntries() {
    user = createUser();
    UserRepositoryService.getInstance().create(user);
    var company = createCompany("Name", "777777777777777");
    CompanyRepositoryService.getInstance().create(company);
    provider = createProductProvider();
    ProductProviderRepositoryService.getInstance().create(provider);
    storage = createProductStorage(company);
    ProductStorageRepositoryService.getInstance().create(storage);
    var category = createCategory("Name");
    CategoryRepositoryService.getInstance().create(category);
    product1 = createProduct("Name 1", category);
    ProductRepositoryService.getInstance().create(product1);
    product2 = createProduct("Name 2", category);
    ProductRepositoryService.getInstance().create(product2);
  }

  @AfterEach
  public void tearDown() {
    mockedDatabase.close();
  }

  @Test
  public void testGetAll() {
    var expected = createOrderItems(product1, product2);
    var order = createOrder(user, storage, provider);
    OrderRepositoryService.getInstance().create(order, expected);
    assertEquals(1, order.getId());
    var actual = orderItemRepository.getAll(order);
    assertEquals(expected.size(), actual.size());
    IntStream.range(0, expected.size())
        .forEach(index -> assertEquals(expected.get(index), actual.get(index)));
  }

  @Test
  public void testGetAllEmpty() {
    var order = createOrder(user, storage, provider);
    order.setId(7);
    assertEquals(0, orderItemRepository.getAll(order).size());
  }
}