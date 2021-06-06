package com.github.ecstasyawesome.warehouse.provider.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.ProductStorageListController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import java.net.URL;

public class ProductStorageListProvider extends
    AbstractModuleProvider<ProductStorageListController> {

  private static final ProductStorageListProvider INSTANCE = new ProductStorageListProvider();
  private final URL fxml = getClass().getResource("/model/storage/ProductStorageList.fxml");

  private ProductStorageListProvider() {
  }

  public static ProductStorageListProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<ProductStorageListController> create() {
    return new FxmlModule<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "Product storage list";  // TODO i18n
  }
}
