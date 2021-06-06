package com.github.ecstasyawesome.warehouse.module.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.NewProductStorageController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.module.AbstractFeedbackModule;
import java.net.URL;

public class NewProductStorageModule extends
    AbstractFeedbackModule<NewProductStorageController, ProductStorage> {

  private static final NewProductStorageModule INSTANCE = new NewProductStorageModule();
  private final URL fxml = getClass().getResource("/model/storage/NewProductStorage.fxml");

  private NewProductStorageModule() {
  }

  public static NewProductStorageModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<NewProductStorageController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "New product storage"; // TODO i18n
  }
}
