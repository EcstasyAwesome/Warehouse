package com.github.ecstasyawesome.warehouse.repository.impl;

import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.OrderItem;
import com.github.ecstasyawesome.warehouse.repository.OrderItemRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderItemRepositoryService extends OrderItemRepository {

  private static final OrderItemRepositoryService INSTANCE = new OrderItemRepositoryService();
  private final ProductRepositoryService productRepositoryService =
      ProductRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(OrderItemRepositoryService.class);

  private OrderItemRepositoryService() {
  }

  public static OrderItemRepositoryService getInstance() {
    return INSTANCE;
  }

  @Override
  public ObservableList<OrderItem> getAll(final Order criteria) {
    Objects.requireNonNull(criteria);
    final var query = """
        SELECT *
        FROM ORDERS_ITEMS
            INNER JOIN PRODUCTS
                ON ORDERS_ITEMS.PRODUCT_ID = PRODUCTS.PRODUCT_ID
        WHERE ORDER_ID=?
        """;
    try {
      var result = selectRecords(query, criteria.getId());
      logger.debug("Selected all order items by order id={} [{} record(s)]", criteria.getId(),
          result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected OrderItem transformToObj(final ResultSet resultSet) throws SQLException {
    return OrderItem.Builder.create()
        .setId(resultSet.getLong("ORDERS_ITEMS.ITEM_ID"))
        .setProduct(productRepositoryService.transformToObj(resultSet))
        .setAmount(resultSet.getDouble("ORDERS_ITEMS.ITEM_AMOUNT"))
        .build();
  }
}
