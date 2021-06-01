package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.ProductStorageList;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class ProductStorageListProvider extends ModuleProvider<ProductStorageList> {

  private static final ProductStorageListProvider INSTANCE = new ProductStorageListProvider();
  private final URL fxml = getClass().getResource("/model/storage/ProductStorageList.fxml");

  private ProductStorageListProvider() {
  }

  public static ProductStorageListProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public Module<ProductStorageList> create() {
    return new Module<>(fxml) {
    };
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
