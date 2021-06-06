package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.ProductStorageListController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.core.module.AbstractModule;
import java.net.URL;

public class ProductStorageListModule extends
    AbstractModule<ProductStorageListController> {

  private static final ProductStorageListModule INSTANCE = new ProductStorageListModule();
  private final URL fxml = getClass().getResource("/model/storage/ProductStorageList.fxml");

  private ProductStorageListModule() {
  }

  public static ProductStorageListModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ProductStorageListController> create() {
    return new FxmlBundle<>(fxml);
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
