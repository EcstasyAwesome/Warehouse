package com.github.ecstasyawesome.warehouse.provider.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.EditProductStorageController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class ShowProductStorageProvider extends
    AbstractConfiguredModuleProvider<EditProductStorageController, ProductStorage> {

  private static final ShowProductStorageProvider INSTANCE = new ShowProductStorageProvider();
  private final URL fxml = getClass().getResource("/model/storage/ShowProductStorage.fxml");

  private ShowProductStorageProvider() {
  }

  public static ShowProductStorageProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractConfiguredModule<EditProductStorageController, ProductStorage> create() {
    return new AbstractConfiguredModule<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "Show product storage"; // TODO i18n
  }
}
