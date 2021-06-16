package com.github.ecstasyawesome.warehouse.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import com.github.ecstasyawesome.warehouse.util.TestDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class CategoryRepositoryServiceTest {

  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private MockedStatic<DatabaseManager> mockedDatabase;

  @SuppressWarnings("SqlWithoutWhere")
  @BeforeEach
  public void setUp() throws SQLException {
    try (var connection = TestDatabase.getConnection();
        var statement = connection.createStatement()) {
      statement.addBatch("DELETE FROM CATEGORIES");
      statement.addBatch("ALTER TABLE CATEGORIES ALTER COLUMN CATEGORY_ID RESTART WITH 1");
      statement.executeBatch();
    }
    mockedDatabase = mockStatic(DatabaseManager.class);
    mockedDatabase.when(DatabaseManager::getConnection)
        .then(answer -> TestDatabase.getConnection());
  }

  @AfterEach
  public void tearDown() {
    mockedDatabase.close();
  }

  @Test
  public void testGetAll() {
    var one = getTestEntry("Name1");
    categoryRepository.create(one);
    var two = getTestEntry("Name2");
    categoryRepository.create(two);
    var three = getTestEntry("Name3");
    categoryRepository.create(three);
    var expected = List.of(one, two, three);
    var actual = categoryRepository.getAll();
    assertEquals(expected.size(), actual.size());
    IntStream.range(0, expected.size())
        .forEach(index -> assertEquals(expected.get(index), actual.get(index)));
  }

  @Test
  public void testGetAllEmptyList() {
    var actual = categoryRepository.getAll();
    assertEquals(0, actual.size());
  }

  @Test
  public void testReadAndCreate() {
    var category = getTestEntry();
    categoryRepository.create(category);
    assertEquals(1, category.getId());
    assertEquals(category, categoryRepository.read(1));
  }

  @Test
  public void testReadAbsentEntry() {
    assertThrows(NullPointerException.class, () -> categoryRepository.read(1));
  }

  @Test
  public void testCreateDuplicate() {
    var category = getTestEntry();
    categoryRepository.create(category);
    assertEquals(1, category.getId());
    assertEquals(category, categoryRepository.read(1));
    assertThrows(NullPointerException.class, () -> categoryRepository.create(category));
  }

  @Test
  public void testUpdate() {
    var category = getTestEntry();
    categoryRepository.create(category);
    category.setName("New name");
    categoryRepository.update(category);
    assertEquals(category, categoryRepository.read(category.getId()));
  }

  @Test
  public void testUpdateDuplicate() {
    var one = getTestEntry("Name1");
    categoryRepository.create(one);
    var two = getTestEntry("Name2");
    categoryRepository.create(two);
    two.setName(one.getName());
    assertThrows(NullPointerException.class, () -> categoryRepository.update(two));
  }

  @Test
  public void testDelete() {
    var category = getTestEntry();
    categoryRepository.create(category);
    assertEquals(1, category.getId());
    categoryRepository.delete(category);
    assertThrows(NullPointerException.class, () -> categoryRepository.read(category.getId()));
  }

  @Test
  public void testDeleteAbsentEntry() {
    var category = getTestEntry();
    category.setId(7);
    assertThrows(NullPointerException.class, () -> categoryRepository.delete(category));
  }

  private Category getTestEntry() {
    return getTestEntry("Name");
  }

  private Category getTestEntry(String name) {
    return Category.Builder.create()
        .setName(name)
        .setDescription("Text")
        .build();
  }
}