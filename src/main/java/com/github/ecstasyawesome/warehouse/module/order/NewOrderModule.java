package com.github.ecstasyawesome.warehouse.module.order;

import com.github.ecstasyawesome.warehouse.controller.order.NewOrderController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractCachedModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class NewOrderModule extends AbstractCachedModule<NewOrderController, NewOrderController> {

  private static final NewOrderModule INSTANCE = new NewOrderModule();
  private final URL fxml = getClass().getResource("/model/order/NewOrder.fxml");

  private NewOrderModule() {
  }

  public static NewOrderModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<NewOrderController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "New order"; // TODO i18n
  }
}
