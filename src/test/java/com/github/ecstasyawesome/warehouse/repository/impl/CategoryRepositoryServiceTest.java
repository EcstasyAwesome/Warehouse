package com.github.ecstasyawesome.warehouse.repository.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCategory;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class CategoryRepositoryServiceTest {

  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
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
      statement.addBatch("DELETE FROM CATEGORIES");
      statement.addBatch("ALTER TABLE CATEGORIES ALTER COLUMN CATEGORY_ID RESTART WITH 1");
      statement.executeBatch();
    }
  }

  @AfterEach
  public void tearDown() {
    mockedDatabase.close();
  }

  @Test
  public void testGetAll() {
    var category1 = createCategory("Name1");
    categoryRepository.create(category1);
    var category2 = createCategory("Name2");
    categoryRepository.create(category2);
    var category3 = createCategory("Name3");
    categoryRepository.create(category3);
    var expected = List.of(category1, category2, category3);
    var actual = categoryRepository.getAll();
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  @Test
  public void testGetAllEmptyList() {
    var actual = categoryRepository.getAll();
    assertEquals(0, actual.size());
  }

  @Test
  public void testReadAndCreate() {
    var category = createCategory("Name");
    categoryRepository.create(category);
    assertEquals(1, category.getId());
    assertEquals(category, categoryRepository.read(category.getId()));
  }

  @Test
  public void testReadAbsentEntry() {
    assertThrows(NullPointerException.class, () -> categoryRepository.read(1));
  }

  @Test
  public void testCreateDuplicate() {
    var category = createCategory("Name");
    categoryRepository.create(category);
    assertEquals(1, category.getId());
    assertEquals(category, categoryRepository.read(category.getId()));
    assertThrows(NullPointerException.class, () -> categoryRepository.create(category));
  }

  @Test
  public void testUpdate() {
    var category = createCategory("Name");
    categoryRepository.create(category);
    category.setName("New name");
    category.setDescription("Some text");
    categoryRepository.update(category);
    assertEquals(category, categoryRepository.read(category.getId()));
  }

  @Test
  public void testUpdateDuplicate() {
    var category1 = createCategory("Name1");
    categoryRepository.create(category1);
    var category2 = createCategory("Name2");
    categoryRepository.create(category2);
    category2.setName(category1.getName());
    assertThrows(NullPointerException.class, () -> categoryRepository.update(category2));
  }

  @Test
  public void testDelete() {
    var category = createCategory("Name");
    categoryRepository.create(category);
    assertEquals(1, category.getId());
    categoryRepository.delete(category);
    assertThrows(NullPointerException.class, () -> categoryRepository.read(category.getId()));
  }

  @Test
  public void testDeleteAbsentEntry() {
    var category = createCategory("Name");
    category.setId(7);
    assertThrows(NullPointerException.class, () -> categoryRepository.delete(category));
  }
}