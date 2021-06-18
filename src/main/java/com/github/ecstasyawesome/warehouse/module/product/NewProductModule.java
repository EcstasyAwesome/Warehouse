package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.NewProductController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import java.net.URL;

public class NewProductModule extends AbstractFeedbackModule<NewProductController, Product> {

  private static final NewProductModule INSTANCE = new NewProductModule();
  private final URL fxml = getClass().getResource("/model/product/NewProduct.fxml");

  private NewProductModule() {
  }

  public static NewProductModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<NewProductController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "New product"; // TODO i18n
  }

}
