package com.github.ecstasyawesome.warehouse.dao.impl;

import com.github.ecstasyawesome.warehouse.dao.CategoryDao;
import com.github.ecstasyawesome.warehouse.model.Category;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryDaoService extends CategoryDao {

  private static final CategoryDaoService INSTANCE = new CategoryDaoService();
  private final Logger logger = LogManager.getLogger(CategoryDaoService.class);

  private CategoryDaoService() {
  }

  public static CategoryDaoService getInstance() {
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
      logger.debug("Selected all categories [{} records]", result.size());
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
  public Category get(final long id) {
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
  public void delete(final long id) {
    try {
      var result = execute("DELETE FROM CATEGORIES WHERE CATEGORY_ID=?", id);
      if (result == 0) {
        throw new SQLException("Deleted nothing");
      }
      logger.debug("Deleted a category with id={}", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected Category transformToObj(final ResultSet resultSet) throws SQLException {
    return Category.Builder.create()
        .setId(resultSet.getLong("CATEGORY_ID"))
        .setName(resultSet.getString("CATEGORY_NAME"))
        .setDescription(resultSet.getString("CATEGORY_DESCRIPTION"))
        .build();
  }
}
