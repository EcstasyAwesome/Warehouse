package com.github.ecstasyawesome.warehouse.dao.impl;

import com.github.ecstasyawesome.warehouse.dao.ProductDao;
import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import com.github.ecstasyawesome.warehouse.model.Unit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
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
  public ObservableList<Product> getAll() {
    final var query = """
        SELECT *
        FROM PRODUCTS
             INNER JOIN CATEGORIES
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
  public ObservableList<Product> getAll(final Category category) {
    Objects.requireNonNull(category);
    final var query = """
        SELECT *
        FROM PRODUCTS
             INNER JOIN CATEGORIES
                        ON PRODUCTS.CATEGORY_ID = CATEGORIES.CATEGORY_ID
        WHERE PRODUCTS.CATEGORY_ID=?
        """;
    try {
      var result = selectRecords(query, category.getId());
      logger.debug("Selected all products by category id={} [{} records]", category.getId(),
          result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<Product> search(String name) {
    Objects.requireNonNull(name);
    final var query = """
        SELECT *
        FROM PRODUCTS
             INNER JOIN CATEGORIES
                        ON PRODUCTS.CATEGORY_ID = CATEGORIES.CATEGORY_ID
        WHERE LOWER(PRODUCT_NAME) LIKE LOWER(?)
        """;
    try {
      var result = selectRecords(query, name);
      logger.debug("Selected all products where name contains '{}' [{} records]", name,
          result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void create(final Product instance) {
    Objects.requireNonNull(instance);
    final var query = """
        INSERT INTO PRODUCTS (CATEGORY_ID, PRODUCT_NAME, PRODUCT_UNIT)
        VALUES (?, ?, ?)
        """;
    try {
      var result = insertRecord(query, instance.getCategory().getId(), instance.getName(),
          instance.getUnit().name());
      instance.setId(result);
      logger.debug("Created a new product with id={}", result);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public Product get(final long id) {
    final var query = """
        SELECT *
        FROM PRODUCTS
             INNER JOIN CATEGORIES
                        ON PRODUCTS.CATEGORY_ID = CATEGORIES.CATEGORY_ID
        WHERE PRODUCT_ID=?
        """;
    try {
      var result = selectRecord(query, id);
      logger.debug("Selected a product with id={}", id);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void update(final Product instance) {
    Objects.requireNonNull(instance);
    final var query = """
        UPDATE PRODUCTS
        SET CATEGORY_ID=?,
            PRODUCT_NAME=?,
            PRODUCT_UNIT=?
        WHERE PRODUCT_ID=?
        """;
    try {
      execute(query, instance.getCategory().getId(), instance.getName(), instance.getUnit().name(),
          instance.getId());
      logger.debug("Updated a user with id={}", instance.getId());
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void delete(final long id) {
    try {
      var result = execute("DELETE FROM PRODUCTS WHERE PRODUCT_ID=?", id);
      if (result == 0) {
        throw new SQLException("Deleted nothing");
      }
      logger.debug("Deleted a product with id={}", id);
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public boolean isFieldUnique(final String name) {
    checkStringParameter(name);
    try {
      var result = !check("SELECT EXISTS(SELECT 1 FROM PRODUCTS WHERE PRODUCT_NAME=?)", name);
      logger.debug("Name '{}' is unique [{}]", name, result);
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected Product transformToObj(final ResultSet resultSet) throws SQLException {
    return Product.getBuilder()
        .setId(resultSet.getLong("PRODUCT_ID"))
        .setCategory(categoryDaoService.transformToObj(resultSet))
        .setName(resultSet.getString("PRODUCT_NAME"))
        .setUnit(Unit.valueOf(resultSet.getString("PRODUCT_UNIT")))
        .build();
  }
}
