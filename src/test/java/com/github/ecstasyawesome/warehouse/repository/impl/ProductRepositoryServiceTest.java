package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.repository.DefaultRecordRepository.createProduct;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.repository.ProductRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ProductRepositoryServiceTest {

  private final ProductRepository productRepository = ProductRepositoryService.getInstance();
  private Category category;
  private MockedStatic<DatabaseManager> mockedDatabase;

  @BeforeEach
  public void setUp() throws SQLException {
    clearDatabase();
    mockedDatabase = mockStatic(DatabaseManager.class);
    mockedDatabase.when(DatabaseManager::getConnection)
        .then(answer -> TestDatabase.getConnection());
    category = createCategory("Name");
    CategoryRepositoryService.getInstance().create(category);
  }

  @SuppressWarnings("SqlWithoutWhere")
  private void clearDatabase() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.addBatch("DELETE FROM ORDERS");
      statement.addBatch("DELETE FROM ORDERS_ITEMS");
      statement.addBatch("DELETE FROM CATEGORIES");
      statement.addBatch("DELETE FROM PRODUCTS");
      statement.addBatch("ALTER TABLE CATEGORIES ALTER COLUMN CATEGORY_ID RESTART WITH 1");
      statement.addBatch("ALTER TABLE PRODUCTS ALTER COLUMN PRODUCT_ID RESTART WITH 1");
      statement.executeBatch();
    }
  }

  @AfterEach
  public void tearDown() {
    mockedDatabase.close();
  }

  @Test
  public void testSearchByName() {
    var product1 = createProduct("Nike", category);
    productRepository.create(product1);
    var product2 = createProduct("Sprite", category);
    productRepository.create(product2);
    var product3 = createProduct("Snickers", category);
    productRepository.create(product3);

    var expected1 = Stream.of(product1, product2, product3)
        .filter(product -> product.getName().contains("i"))
        .toList();
    var actual1 = productRepository.search("i");
    assertArrayEquals(expected1.toArray(), actual1.toArray());

    var expected2 = Stream.of(product1, product2, product3)
        .filter(product -> product.getName().toLowerCase().contains("spr"))
        .toList();
    var actual2 = productRepository.search("spr");
    assertArrayEquals(expected2.toArray(), actual2.toArray());
  }

  @Test
  public void testSearchByNameEmpty() {
    assertEquals(0, productRepository.search("some criteria").size());
  }

  @Test
  public void testGetAllByCategory() {
    var product1 = createProduct("Nike", category);
    productRepository.create(product1);
    var product2 = createProduct("Sprite", category);
    productRepository.create(product2);
    var newCategory = createCategory("Food");
    CategoryRepositoryService.getInstance().create(newCategory);
    var product3 = createProduct("Snickers", newCategory);
    productRepository.create(product3);

    var expected1 = List.of(product1, product2);
    var actual1 = productRepository.getAll(category);
    assertArrayEquals(expected1.toArray(), actual1.toArray());

    var expected2 = List.of(product3);
    var actual2 = productRepository.getAll(newCategory);
    assertArrayEquals(expected2.toArray(), actual2.toArray());
  }

  @Test
  public void testGetAllByCategoryEmpty() {
    assertEquals(0, productRepository.getAll(category).size());
  }

  @Test
  public void testGetAll() {
    var product1 = createProduct("Name1", category);
    productRepository.create(product1);
    var product2 = createProduct("Name2", category);
    productRepository.create(product2);
    var product3 = createProduct("Name3", category);
    productRepository.create(product3);
    var expected = List.of(product1, product2, product3);
    var actual = productRepository.getAll();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  public void testGetAllEmptyList() {
    var actual = productRepository.getAll();
    assertEquals(0, actual.size());
  }

  @Test
  public void testReadAndCreate() {
    var product = createProduct("Name", category);
    productRepository.create(product);
    assertEquals(1, product.getId());
    assertEquals(product, productRepository.read(product.getId()));
  }

  @Test
  public void testReadAbsentEntry() {
    assertThrows(NullPointerException.class, () -> productRepository.read(1));
  }

  @Test
  public void testCreateDuplicate() {
    var product = createProduct("Name", category);
    productRepository.create(product);
    assertEquals(1, product.getId());
    assertEquals(product, productRepository.read(product.getId()));
    assertThrows(NullPointerException.class, () -> productRepository.create(product));
  }

  @Test
  public void testUpdate() {
    var product = createProduct("Name", category);
    productRepository.create(product);
    var newCategory = createCategory("New category");
    CategoryRepositoryService.getInstance().create(newCategory);
    product.setName("New name");
    product.setCategory(newCategory);
    product.setUnit(Unit.KILO);
    productRepository.update(product);
    assertEquals(product, productRepository.read(product.getId()));
  }

  @Test
  public void testUpdateDuplicate() {
    var product1 = createProduct("Name1", category);
    productRepository.create(product1);
    var product2 = createProduct("Name2", category);
    productRepository.create(product2);
    product2.setName(product1.getName());
    assertThrows(NullPointerException.class, () -> productRepository.update(product2));
  }

  @Test
  public void testDelete() {
    var product = createProduct("Name", category);
    productRepository.create(product);
    assertEquals(1, product.getId());
    productRepository.delete(product);
    assertThrows(NullPointerException.class,
        () -> productRepository.read(product.getId()));
  }

  @Test
  public void testDeleteAbsentEntry() {
    var product = createProduct("Name", category);
    product.setId(7);
    assertThrows(NullPointerException.class, () -> productRepository.delete(product));
  }

  @Test
  public void checkCascadeDeletingByCategory() {
    productRepository.create(createProduct("Name", category));
    assertEquals(1, productRepository.getAll(category).size());
    CategoryRepositoryService.getInstance().delete(category);
    assertEquals(0, productRepository.getAll(category).size());
  }
}