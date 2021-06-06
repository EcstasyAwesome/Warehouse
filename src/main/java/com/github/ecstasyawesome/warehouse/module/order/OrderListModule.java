package com.github.ecstasyawesome.warehouse.module.order;

import com.github.ecstasyawesome.warehouse.controller.order.OrderListController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class OrderListModule extends AbstractModule<OrderListController> {

  private static final OrderListModule INSTANCE = new OrderListModule();
  private final URL fxml = getClass().getResource("/model/order/OrderList.fxml");

  private OrderListModule() {
  }

  public static OrderListModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<OrderListController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Order list"; // TODO i18n
  }
}
