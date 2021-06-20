package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createOrder;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createOrderItems;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProduct;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductProvider;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductStorage;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.repository.OrderRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class OrderRepositoryServiceTest {

  private final OrderRepository orderRepository = OrderRepositoryService.getInstance();
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
      statement.addBatch("DELETE FROM USERS");
      statement.addBatch("DELETE FROM COMPANIES");
      statement.addBatch("DELETE FROM CATEGORIES");
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
  public void testSearchByPeaceOfId() {
    var orders = new ArrayList<Order>();
    IntStream.range(0, 20).forEach(index -> {
      var order = createOrder(user, storage, provider);
      orderRepository.create(order, createOrderItems(product1, product2));
      orders.add(order);
    });
    var expected1 = orders.stream()
        .filter(order -> String.valueOf(order.getId()).contains("1"))
        .toList();
    var actual1 = orderRepository.search("1");
    assertArrayEquals(expected1.toArray(), actual1.toArray());

    var expected2 = orders.stream()
        .filter(order -> String.valueOf(order.getId()).contains("2"))
        .toList();
    var actual2 = orderRepository.search("2");
    assertArrayEquals(expected2.toArray(), actual2.toArray());
  }

  @Test
  public void testSearchByPeaceOfIdEmpty() {
    assertEquals(0, orderRepository.search("1").size());
  }

  @Test
  public void testSearchByTimeDate() {
    var random = new Random();
    var time = LocalTime.now();
    var orders = new ArrayList<Order>();
    IntStream.range(0, 30).forEach(index -> {
      var order = createOrder(user, storage, provider);
      var date = LocalDate.of(1994, 7, random.nextInt(7) + 1);
      order.setTime(LocalDateTime.of(date, time).truncatedTo(ChronoUnit.MILLIS));
      orderRepository.create(order, createOrderItems(product1, product2));
      orders.add(order);
    });
    var expected = orders.stream()
        .filter(order -> order.getTime().getDayOfMonth() == 7)
        .toList();
    var actual = orderRepository.search(LocalDate.of(1994, 7, 7));
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  public void testGetAll() {
    var expected = new ArrayList<Order>();
    IntStream.range(0, 20).forEach(index -> {
      var order = createOrder(user, storage, provider);
      orderRepository.create(order, createOrderItems(product1, product2));
      expected.add(order);
    });
    var actual = orderRepository.getAll();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  public void testGetAllEmpty() {
    assertEquals(0, orderRepository.getAll().size());
  }

  @Test
  public void testCreate() {
    var order = createOrder(user, storage, provider);
    var items = createOrderItems(product1, product2);
    orderRepository.create(order, items);
    assertEquals(1, order.getId());
    assertEquals(1, items.get(0).getId());
    assertEquals(2, items.get(1).getId());
    var orders = orderRepository.getAll();
    assertEquals(1, orders.size());
    assertEquals(order, orders.get(0));
  }

  @Test
  public void checkCascadeDeletingByProductStorage() {
    var order = createOrder(user, storage, provider);
    orderRepository.create(order, createOrderItems(product1, product2));
    assertEquals(1, orderRepository.getAll().size());
    ProductStorageRepositoryService.getInstance().delete(storage);
    assertEquals(0, orderRepository.getAll().size());
  }

  @Test
  public void checkCascadeDeletingByProductProvider() {
    var order = createOrder(user, storage, provider);
    orderRepository.create(order, createOrderItems(product1, product2));
    assertEquals(1, orderRepository.getAll().size());
    ProductProviderRepositoryService.getInstance().delete(provider);
    assertEquals(0, orderRepository.getAll().size());
  }

  @Test
  public void checkCascadeDeletingByUser() {
    var order = createOrder(user, storage, provider);
    orderRepository.create(order, createOrderItems(product1, product2));
    assertEquals(1, orderRepository.getAll().size());
    assertThrows(NullPointerException.class,
        () -> UserRepositoryService.getInstance().delete(user));
    assertEquals(1, orderRepository.getAll().size());
  }
}