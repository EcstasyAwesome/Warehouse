package com.github.ecstasyawesome.warehouse.module.product;

import com.github.ecstasyawesome.warehouse.controller.product.ProductPickerController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import java.net.URL;
import java.util.HashSet;

public class ProductPickerModule extends
    AbstractFeedbackModule<ProductPickerController, HashSet<Product>> {

  private static final ProductPickerModule INSTANCE = new ProductPickerModule();
  private final URL fxml = getClass().getResource("/model/product/ProductPicker.fxml");

  private ProductPickerModule() {
  }

  public static ProductPickerModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ProductPickerController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Product picker"; // TODO i18n
  }
}
