package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createOrder;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createOrderItems;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createProduct;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createProductProvider;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createProductStorage;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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

  private final OrderItemRepository orderItemRepository = OrderItemRepositoryService.getInstance();
  private User user;
  private ProductProvider provider;
  private ProductStorage storage;
  private Product product1;
  private Product product2;
  private MockedStatic<DatabaseManager> mockedDatabase;

  @BeforeEach
  public void setUp() throws SQLException {
    clearDatabase();
    mockedDatabase = mockStatic(DatabaseManager.class);
    mockedDatabase.when(DatabaseManager::getConnection)
        .then(answer -> TestDatabase.getConnection());
    initDatabaseEntries();
  }

  @SuppressWarnings("SqlWithoutWhere")
  private void clearDatabase() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.addBatch("DELETE FROM ORDERS");
      statement.addBatch("DELETE FROM ORDERS_ITEMS");
      statement.addBatch("DELETE FROM COMPANIES");
      statement.addBatch("DELETE FROM CATEGORIES");
      statement.addBatch("DELETE FROM USERS");
      statement.addBatch("DELETE FROM PRODUCT_PROVIDERS");
      statement.addBatch("DELETE FROM PRODUCT_STORAGES");
      statement.addBatch("DELETE FROM PRODUCTS");
      statement.addBatch("ALTER TABLE ORDERS ALTER COLUMN ORDER_ID RESTART WITH 1");
      statement.addBatch("ALTER TABLE ORDERS_ITEMS ALTER COLUMN ITEM_ID RESTART WITH 1");
      statement.executeBatch();
    }
  }

  private void initDatabaseEntries() {
    user = createUser("login");
    UserRepositoryService.getInstance().create(user);
    var company = createCompany("Name", "777777777777777");
    CompanyRepositoryService.getInstance().create(company);
    provider = createProductProvider("Name");
    ProductProviderRepositoryService.getInstance().create(provider);
    storage = createProductStorage("Name", company);
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

  @Test
  public void checkCascadeDeletingByOrder() {
    var order = createOrder(user, storage, provider);
    OrderRepositoryService.getInstance().create(order, createOrderItems(product1, product2));
    assertEquals(2, orderItemRepository.getAll(order).size());
    try (var connection = TestDatabase.getConnection();
        var statement = connection.prepareStatement("DELETE FROM ORDERS WHERE ORDER_ID=?")) {
      statement.setLong(1, order.getId());
      statement.execute();
    } catch (SQLException exception) {
      fail(exception);
    }
    assertEquals(0, orderItemRepository.getAll(order).size());
  }

  @Test
  public void checkCascadeDeletingByProduct() {
    var order = createOrder(user, storage, provider);
    var items = createOrderItems(product1, product2);
    OrderRepositoryService.getInstance().create(order, items);
    assertEquals(2, orderItemRepository.getAll(order).size());
    assertThrows(NullPointerException.class,
        () -> ProductRepositoryService.getInstance().delete(items.get(0).getProduct()));
    assertThrows(NullPointerException.class,
        () -> ProductRepositoryService.getInstance().delete(items.get(1).getProduct()));
    assertEquals(2, orderItemRepository.getAll(order).size());
  }
}