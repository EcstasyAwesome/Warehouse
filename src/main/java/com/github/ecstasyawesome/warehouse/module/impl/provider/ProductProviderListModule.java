package com.github.ecstasyawesome.warehouse.module.impl.provider;

import com.github.ecstasyawesome.warehouse.controller.impl.provider.ProductProviderListController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import java.net.URL;

public class ProductProviderListModule extends
    AbstractModule<ProductProviderListController> {

  private static final ProductProviderListModule INSTANCE = new ProductProviderListModule();
  private final URL fxml = getClass().getResource("/model/provider/ProductProviderList.fxml");

  private ProductProviderListModule() {
  }

  public static ProductProviderListModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ProductProviderListController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Product provider list";  // TODO i18n
  }
}
