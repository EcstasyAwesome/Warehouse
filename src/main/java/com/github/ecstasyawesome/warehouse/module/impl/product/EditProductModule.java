package com.github.ecstasyawesome.warehouse.module.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.EditProductController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import java.net.URL;

public class EditProductModule extends
    AbstractConfiguredModule<EditProductController, Product> {

  private static final EditProductModule INSTANCE = new EditProductModule();
  private final URL fxml = getClass().getResource("/model/product/EditProduct.fxml");

  private EditProductModule() {
  }

  public static EditProductModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<EditProductController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Edit product"; // TODO i18n
  }
}
