package com.github.ecstasyawesome.warehouse.module.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.EditProductStorageController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import java.net.URL;

public class EditProductStorageModule extends
    AbstractConfiguredModule<EditProductStorageController, ProductStorage> {

  private static final EditProductStorageModule INSTANCE = new EditProductStorageModule();
  private final URL fxml = getClass().getResource("/model/storage/EditProductStorage.fxml");

  private EditProductStorageModule() {
  }

  public static EditProductStorageModule getInstance() {
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
    return "Edit product storage"; // TODO i18n
  }
}
