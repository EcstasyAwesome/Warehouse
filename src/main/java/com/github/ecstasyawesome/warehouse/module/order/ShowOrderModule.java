package com.github.ecstasyawesome.warehouse.module.order;

import com.github.ecstasyawesome.warehouse.controller.order.ShowOrderController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Order;
import java.net.URL;

public class ShowOrderModule extends AbstractConfiguredModule<ShowOrderController, Order> {

  private static final ShowOrderModule INSTANCE = new ShowOrderModule();
  private final URL fxml = getClass().getResource("/model/order/ShowOrder.fxml");

  private ShowOrderModule() {
  }

  public static ShowOrderModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ShowOrderController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Show order"; // TODO i18n
  }
}
