package com.github.ecstasyawesome.warehouse.dao;

import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import com.github.ecstasyawesome.warehouse.model.Unit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductDaoService extends ProductDao {

  private static final ProductDaoService INSTANCE = new ProductDaoService();
  private final CategoryDaoService categoryDaoService = CategoryDaoService.getInstance();
  private final Logger logger = LogManager.getLogger(ProductDaoService.class);

  private ProductDaoService() {
  }

  public static ProductDaoService getInstance() {
    return INSTANCE;
  }

  @Override
  public List<Product> getAll() {
    final var query = """
        SELECT *
        FROM PRODUCTS INNER JOIN CATEGORIES
        ON PRODUCTS.CATEGORY_ID = CATEGORIES.CATEGORY_ID
        """;
    try {
      var result = selectRecords(query);
      logger.debug("Selected all products [{} records]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public long create(final Product instance) {
    Objects.requireNonNull(instance);
    final var query = """
        INSERT INTO PRODUCTS (PRODUCT_NAME, PRODUCT_UNIT, CATEGORY_ID)
        VALUES (?,?,?);
        """;
    try {
      var result = insertRecord(query, instance);
      logger.debug("Created a new product with id={}", result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public Product get(final long id) {
    final var query = String.format("""
        SELECT *
        FROM PRODUCTS INNER JOIN CATEGORIES
        ON PRODUCTS.CATEGORY_ID = CATEGORIES.CATEGORY_ID
        WHERE PRODUCT_ID=%d
        """, id);
    try {
      var result = selectRecord(query);
      logger.debug("Selected a product with id={}", result.getId());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public Product get(final Category category) {
    Objects.requireNonNull(category);
    final var query = String.format("""
        SELECT *
        FROM PRODUCTS INNER JOIN CATEGORIES
        ON PRODUCTS.CATEGORY_ID = CATEGORIES.CATEGORY_ID
        WHERE CATEGORY_ID=%d
        """, category.getId());
    try {
      var result = selectRecord(query);
      logger.debug("Selected a product with id={} by category id={}", result.getId(),
          category.getId());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(final Product instance) {
    Objects.requireNonNull(instance);
    final var query = String.format("""
        UPDATE PRODUCTS
        SET PRODUCT_NAME=?,
            PRODUCT_UNIT=?,
            CATEGORY_ID=?
        WHERE PRODUCT_ID=%d
        """, instance.getId());
    try {
      processRecord(query, instance);
      logger.debug("Updated a user with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final long id) {
    final var query = String.format("DELETE FROM PRODUCTS WHERE PRODUCT_ID=%d", id);
    try {
      processRecord(query);
      logger.debug("Deleted a product with id={}", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public boolean isNamePresent(final String name) {
    checkStringParameter(name);
    final var query = String.format("SELECT * FROM PRODUCTS WHERE PRODUCT_NAME='%s'", name);
    try {
      var result = hasQueryResult(query);
      logger.debug("Name '{}' is present [{}]", name, result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected void serialize(final PreparedStatement statement, final Product instance)
      throws SQLException {
    statement.setString(1, instance.getName());
    statement.setString(2, instance.getUnit().name());
    statement.setLong(3, instance.getCategory().getId());
  }

  @Override
  protected Product deserialize(final ResultSet resultSet) throws SQLException {
    return Product.builder()
        .id(resultSet.getLong("PRODUCT_ID"))
        .name(resultSet.getString("PRODUCT_NAME"))
        .category(categoryDaoService.deserialize(resultSet))
        .unit(Unit.valueOf(resultSet.getString("PRODUCT_UNIT")))
        .build();
  }
}
