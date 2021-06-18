package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.ShowProductController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import java.net.URL;

public class ShowProductModule extends AbstractConfiguredModule<ShowProductController, Product> {

  private static final ShowProductModule INSTANCE = new ShowProductModule();
  private final URL fxml = getClass().getResource("/model/product/ShowProduct.fxml");

  private ShowProductModule() {
  }

  public static ShowProductModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ShowProductController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Show product"; // TODO i18n
  }
}
