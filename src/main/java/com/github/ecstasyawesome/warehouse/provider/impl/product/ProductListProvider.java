package com.github.ecstasyawesome.warehouse.provider.impl.product;

import com.github.ecstasyawesome.warehouse.controller.impl.product.ProductListController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import java.net.URL;

public class ProductListProvider extends AbstractModuleProvider<ProductListController> {

  private static final ProductListProvider INSTANCE = new ProductListProvider();
  private final URL fxml = getClass().getResource("/model/product/ProductList.fxml");

  private ProductListProvider() {
  }

  public static ProductListProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractModule<ProductListController> create() {
    return new AbstractModule<>(fxml) {
    };
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
