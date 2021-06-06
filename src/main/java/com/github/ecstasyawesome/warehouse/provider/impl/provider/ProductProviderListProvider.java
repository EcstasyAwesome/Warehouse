package com.github.ecstasyawesome.warehouse.provider.impl.provider;

import com.github.ecstasyawesome.warehouse.controller.impl.provider.ProductProviderListController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import java.net.URL;

public class ProductProviderListProvider extends
    AbstractModuleProvider<ProductProviderListController> {

  private static final ProductProviderListProvider INSTANCE = new ProductProviderListProvider();
  private final URL fxml = getClass().getResource("/model/provider/ProductProviderList.fxml");

  private ProductProviderListProvider() {
  }

  public static ProductProviderListProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<ProductProviderListController> create() {
    return new FxmlModule<>(fxml);
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
