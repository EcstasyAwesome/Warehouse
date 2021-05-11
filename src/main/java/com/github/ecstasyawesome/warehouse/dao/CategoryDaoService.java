package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.Category;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
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
  public boolean isNamePresent(final String name) {
    checkStringParameter(name);
    final var query = String.format("SELECT * FROM CATEGORIES WHERE CATEGORY_NAME='%s'", name);
    try {
      var result = hasQueryResult(query);
      logger.debug("Name '{}' is present [{}]", name, result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public List<Category> getAll() {
    try {
      var result = selectRecords("SELECT * FROM CATEGORIES");
      logger.debug("Selected all categories [{} records]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public long create(final Category instance) {
    Objects.requireNonNull(instance);
    final var query = """
        INSERT INTO CATEGORIES (CATEGORY_NAME)
        VALUES (?);
        """;
    try {
      var result = insertRecord(query, instance);
      logger.debug("Created a new category with id={}", result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public Category get(final long id) {
    final var query = String.format("SELECT * FROM CATEGORIES WHERE CATEGORY_ID=%d", id);
    try {
      var result = selectRecord(query);
      logger.debug("Selected a category with id={}", result.getId());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(final Category instance) {
    Objects.requireNonNull(instance);
    final var query = String.format("""
        UPDATE CATEGORIES
        SET CATEGORY_NAME=?
        WHERE CATEGORY_ID=%d
        """, instance.getId());
    try {
      processRecord(query, instance);
      logger.debug("Updated a category with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final long id) {
    final var query = String.format("DELETE * FROM CATEGORIES WHERE CATEGORY_ID=%d", id);
    try {
      processRecord(query);
      logger.debug("Deleted a category with id={}", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected void serialize(final PreparedStatement statement, final Category instance)
      throws SQLException {
    statement.setString(1, instance.getName());
  }

  @Override
  protected Category deserialize(final ResultSet resultSet) throws SQLException {
    return Category.builder()
        .id(resultSet.getLong("CATEGORY_ID"))
        .name(resultSet.getString("CATEGORY_NAME")).build();
  }
}
