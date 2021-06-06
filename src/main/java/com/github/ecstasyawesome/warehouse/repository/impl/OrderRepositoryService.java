package com.github.ecstasyawesome.warehouse.repository.impl;

import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.OrderItem;
import com.github.ecstasyawesome.warehouse.repository.OrderRepository;
import com.github.ecstasyawesome.warehouse.util.DatabaseManager;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderRepositoryService extends OrderRepository {

  private static final OrderRepositoryService INSTANCE = new OrderRepositoryService();
  private final UserRepositoryService userRepositoryService = UserRepositoryService.getInstance();
  private final ProductProviderRepositoryService productProviderRepositoryService =
      ProductProviderRepositoryService.getInstance();
  private final ProductStorageRepositoryService productStorageRepositoryService =
      ProductStorageRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(OrderRepositoryService.class);

  private OrderRepositoryService() {
  }

  public static OrderRepositoryService getInstance() {
    return INSTANCE;
  }

  @Override
  public ObservableList<Order> search(String partOfId) {
    Objects.requireNonNull(partOfId);
    final var query = """
        SELECT *
        FROM ORDERS
            INNER JOIN PRODUCT_PROVIDERS
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS.PROVIDER_ID
            INNER JOIN PRODUCT_PROVIDERS_CONTACTS
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS_CONTACTS.PROVIDER_ID
            INNER JOIN PRODUCT_PROVIDERS_ADDRESSES
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS_ADDRESSES.PROVIDER_ID
            INNER JOIN PRODUCT_STORAGES
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_CONTACTS
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES_CONTACTS.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_ADDRESSES
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES_ADDRESSES.STORAGE_ID
            INNER JOIN COMPANIES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES.COMPANY_ID
            INNER JOIN COMPANIES_CONTACTS
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_CONTACTS.COMPANY_ID
            INNER JOIN COMPANIES_ADDRESSES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_ADDRESSES.COMPANY_ID
            INNER JOIN USERS
                ON ORDERS.USER_ID = USERS.USER_ID
            INNER JOIN USERS_CONTACTS
                ON ORDERS.USER_ID = USERS_CONTACTS.USER_ID
            INNER JOIN USERS_SECURITY
                ON ORDERS.USER_ID = USERS_SECURITY.USER_ID
        WHERE ORDER_ID LIKE ?
        """;
    try {
      var result = selectRecords(query, String.format("%%%s%%", partOfId));
      logger.debug("Selected all orders where id contains '{}' [{} record(s)]", partOfId,
          result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<Order> search(LocalDate date) {
    Objects.requireNonNull(date);
    final var query = """
        SELECT *
        FROM ORDERS
            INNER JOIN PRODUCT_PROVIDERS
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS.PROVIDER_ID
            INNER JOIN PRODUCT_PROVIDERS_CONTACTS
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS_CONTACTS.PROVIDER_ID
            INNER JOIN PRODUCT_PROVIDERS_ADDRESSES
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS_ADDRESSES.PROVIDER_ID
            INNER JOIN PRODUCT_STORAGES
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_CONTACTS
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES_CONTACTS.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_ADDRESSES
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES_ADDRESSES.STORAGE_ID
            INNER JOIN COMPANIES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES.COMPANY_ID
            INNER JOIN COMPANIES_CONTACTS
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_CONTACTS.COMPANY_ID
            INNER JOIN COMPANIES_ADDRESSES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_ADDRESSES.COMPANY_ID
            INNER JOIN USERS
                ON ORDERS.USER_ID = USERS.USER_ID
            INNER JOIN USERS_CONTACTS
                ON ORDERS.USER_ID = USERS_CONTACTS.USER_ID
            INNER JOIN USERS_SECURITY
                ON ORDERS.USER_ID = USERS_SECURITY.USER_ID
        WHERE ORDER_TIME LIKE ?
        """;
    try {
      var result = selectRecords(query, String.format("%%%s%%", Date.valueOf(date)));
      logger.debug("Selected all orders where date is '{}' [{} record(s)]", date, result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public ObservableList<Order> getAll() {
    final var query = """
        SELECT *
        FROM ORDERS
            INNER JOIN PRODUCT_PROVIDERS
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS.PROVIDER_ID
            INNER JOIN PRODUCT_PROVIDERS_CONTACTS
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS_CONTACTS.PROVIDER_ID
            INNER JOIN PRODUCT_PROVIDERS_ADDRESSES
                ON ORDERS.PROVIDER_ID = PRODUCT_PROVIDERS_ADDRESSES.PROVIDER_ID
            INNER JOIN PRODUCT_STORAGES
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_CONTACTS
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES_CONTACTS.STORAGE_ID
            INNER JOIN PRODUCT_STORAGES_ADDRESSES
                ON ORDERS.STORAGE_ID = PRODUCT_STORAGES_ADDRESSES.STORAGE_ID
            INNER JOIN COMPANIES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES.COMPANY_ID
            INNER JOIN COMPANIES_CONTACTS
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_CONTACTS.COMPANY_ID
            INNER JOIN COMPANIES_ADDRESSES
                ON PRODUCT_STORAGES.COMPANY_ID = COMPANIES_ADDRESSES.COMPANY_ID
            INNER JOIN USERS
                ON ORDERS.USER_ID = USERS.USER_ID
            INNER JOIN USERS_CONTACTS
                ON ORDERS.USER_ID = USERS_CONTACTS.USER_ID
            INNER JOIN USERS_SECURITY
                ON ORDERS.USER_ID = USERS_SECURITY.USER_ID
        """;
    try {
      var result = selectRecords(query);
      logger.debug("Selected all orders [{} record(s)]", result.size());
      return result;
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  public void create(final Order instance, final List<OrderItem> items) {
    Objects.requireNonNull(instance);
    final var orderQuery = """
        INSERT INTO ORDERS (PROVIDER_ID, STORAGE_ID, USER_ID, ORDER_TIME, ORDER_COMMENT)
        VALUES (?, ?, ?, ?, ?)
        """;
    final var orderItemQuery = """
        INSERT INTO ORDERS_ITEMS (ORDER_ID, PRODUCT_ID, ITEM_AMOUNT)
        VALUES (?, ?, ?)
        """;
    try (var connection = DatabaseManager.getConnection()) {
      connection.setAutoCommit(false);
      try {
        final var orderId = insertRecord(connection, orderQuery,
            instance.getProductProvider().getId(),
            instance.getProductStorage().getId(),
            instance.getUser().getId(),
            Timestamp.valueOf(instance.getTime()),
            instance.getComment());
        final var ids = new ArrayList<Long>();
        for (var item : items) {
          var itemId = insertRecord(connection, orderItemQuery, orderId, item.getProduct().getId(),
              item.getAmount());
          ids.add(itemId);
        }
        connection.commit();
        var index = 0;
        for (var id : ids) {
          items.get(index++).setId(id);
        }
        instance.setId(orderId);
        logger.debug("Created a new order with id={}", orderId);
      } catch (SQLException exception) {
        connection.rollback();
        throw exception;
      }
    } catch (SQLException exception) {
      throw createNpeWithSuppressedException(logger.throwing(Level.ERROR, exception));
    }
  }

  @Override
  protected Order transformToObj(final ResultSet resultSet) throws SQLException {
    return Order.Builder.create()
        .setId(resultSet.getLong("ORDERS.ORDER_ID"))
        .setProductProvider(productProviderRepositoryService.transformToObj(resultSet))
        .setProductStorage(productStorageRepositoryService.transformToObj(resultSet))
        .setTime(resultSet.getTimestamp("ORDERS.ORDER_TIME").toLocalDateTime())
        .setUser(userRepositoryService.transformToObj(resultSet))
        .setComment(resultSet.getString("ORDERS.ORDER_COMMENT"))
        .build();
  }
}
