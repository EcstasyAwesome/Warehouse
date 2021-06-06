package com.github.ecstasyawesome.warehouse.module.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.ProductListController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import java.net.URL;

public class ProductListModule extends AbstractModule<ProductListController> {

  private static final ProductListModule INSTANCE = new ProductListModule();
  private final URL fxml = getClass().getResource("/model/product/ProductList.fxml");

  private ProductListModule() {
  }

  public static ProductListModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ProductListController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Product list"; // TODO i18n
  }
}
