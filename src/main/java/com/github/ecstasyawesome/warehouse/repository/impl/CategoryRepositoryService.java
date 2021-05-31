package com.github.ecstasyawesome.warehouse.repository.impl;

import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryRepositoryService extends CategoryRepository {

  private static final CategoryRepositoryService INSTANCE = new CategoryRepositoryService();
  private final Logger logger = LogManager.getLogger(CategoryRepositoryService.class);

  private CategoryRepositoryService() {
  }

  public static CategoryRepositoryService getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isFieldUnique(final String name) {
    checkStringParameter(name);
    try {
      var result = !check("SELECT EXISTS(SELECT 1 FROM CATEGORIES WHERE CATEGORY_NAME=?)", name);
      logger.debug("Name '{}' is unique [{}]", name, result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<Category> getAll() {
    try {
      var result = selectRecords("SELECT * FROM CATEGORIES");
      logger.debug("Selected all categories [{} record(s)]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void create(final Category instance) {
    Objects.requireNonNull(instance);
    final var query = """
        INSERT INTO CATEGORIES (CATEGORY_NAME, CATEGORY_DESCRIPTION)
        VALUES (?, ?)
        """;
    try {
      var result = insertRecord(query, instance.getName(), instance.getDescription());
      instance.setId(result);
      logger.debug("Created a new category with id={}", result);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public Category read(final long id) {
    try {
      var result = selectRecord("SELECT * FROM CATEGORIES WHERE CATEGORY_ID=?", id);
      logger.debug("Selected a category with id={}", id);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(final Category instance) {
    Objects.requireNonNull(instance);
    final var query = """
        UPDATE CATEGORIES
        SET CATEGORY_NAME=?,
            CATEGORY_DESCRIPTION=?
        WHERE CATEGORY_ID=?
        """;
    try {
      execute(query, instance.getName(), instance.getDescription(), instance.getId());
      logger.debug("Updated a category with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final Category instance) {
    try {
      var result = execute("DELETE FROM CATEGORIES WHERE CATEGORY_ID=?", instance.getId());
      if (result == 0) {
        throw new SQLException("Deleted nothing");
      }
      logger.debug("Deleted a category with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected Category transformToObj(final ResultSet resultSet) throws SQLException {
    return Category.Builder.create()
        .setId(resultSet.getLong("CATEGORIES.CATEGORY_ID"))
        .setName(resultSet.getString("CATEGORIES.CATEGORY_NAME"))
        .setDescription(resultSet.getString("CATEGORIES.CATEGORY_DESCRIPTION"))
        .build();
  }
}
