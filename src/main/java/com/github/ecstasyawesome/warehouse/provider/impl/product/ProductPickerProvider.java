package com.github.ecstasyawesome.warehouse.provider.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.ProductPickerController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractFeedbackModuleProvider;
import java.net.URL;
import java.util.HashSet;

public class ProductPickerProvider extends
    AbstractFeedbackModuleProvider<ProductPickerController, HashSet<Product>> {

  private static final ProductPickerProvider INSTANCE = new ProductPickerProvider();
  private final URL fxml = getClass().getResource("/model/product/ProductPicker.fxml");

  private ProductPickerProvider() {
  }

  public static ProductPickerProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<ProductPickerController> create() {
    return new FxmlModule<>(fxml);
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
