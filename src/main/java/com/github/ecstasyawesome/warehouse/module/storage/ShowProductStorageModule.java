package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.EditProductStorageController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import java.net.URL;

public class ShowProductStorageModule extends
    AbstractConfiguredModule<EditProductStorageController, ProductStorage> {

  private static final ShowProductStorageModule INSTANCE = new ShowProductStorageModule();
  private final URL fxml = getClass().getResource("/model/storage/ShowProductStorage.fxml");

  private ShowProductStorageModule() {
  }

  public static ShowProductStorageModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<EditProductStorageController> create() {
    return new FxmlBundle<>(fxml);
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
