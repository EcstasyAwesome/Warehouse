package com.github.ecstasyawesome.warehouse.provider.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.NewProductStorageController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractFeedbackModuleProvider;
import java.net.URL;

public class NewProductStorageProvider extends
    AbstractFeedbackModuleProvider<NewProductStorageController, ProductStorage> {

  private static final NewProductStorageProvider INSTANCE = new NewProductStorageProvider();
  private final URL fxml = getClass().getResource("/model/storage/NewProductStorage.fxml");

  private NewProductStorageProvider() {
  }

  public static NewProductStorageProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<NewProductStorageController> create() {
    return new FxmlModule<>(fxml);
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
