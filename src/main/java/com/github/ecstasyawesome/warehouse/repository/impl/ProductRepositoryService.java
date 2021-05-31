package com.github.ecstasyawesome.warehouse.repository.impl;

import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.repository.ProductRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductRepositoryService extends ProductRepository {

  private static final ProductRepositoryService INSTANCE = new ProductRepositoryService();
  private final CategoryRepositoryService categoryRepositoryService =
      CategoryRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(ProductRepositoryService.class);

  private ProductRepositoryService() {
  }

  public static ProductRepositoryService getInstance() {
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
      logger.debug("Selected all products [{} record(s)]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<Product> getAll(final Category criteria) {
    Objects.requireNonNull(criteria);
    final var query = """
        SELECT *
        FROM PRODUCTS
            INNER JOIN CATEGORIES
                ON PRODUCTS.CATEGORY_ID = CATEGORIES.CATEGORY_ID
        WHERE PRODUCTS.CATEGORY_ID=?
        """;
    try {
      var result = selectRecords(query, criteria.getId());
      logger.debug("Selected all products by category id={} [{} record(s)]", criteria.getId(),
          result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<Product> search(final String name) {
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
      logger.debug("Selected all products where name contains '{}' [{} record(s)]", name,
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
  public Product read(final long id) {
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
  public void delete(final Product instance) {
    try {
      var result = execute("DELETE FROM PRODUCTS WHERE PRODUCT_ID=?", instance.getId());
      if (result == 0) {
        throw new SQLException("Deleted nothing");
      }
      logger.debug("Deleted a product with id={}", instance.getId());
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
    return Product.Builder.create()
        .setId(resultSet.getLong("PRODUCTS.PRODUCT_ID"))
        .setCategory(categoryRepositoryService.transformToObj(resultSet))
        .setName(resultSet.getString("PRODUCTS.PRODUCT_NAME"))
        .setUnit(Unit.valueOf(resultSet.getString("PRODUCTS.PRODUCT_UNIT")))
        .build();
  }
}
