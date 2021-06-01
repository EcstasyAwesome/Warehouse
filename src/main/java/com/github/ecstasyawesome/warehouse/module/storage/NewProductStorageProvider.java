package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.NewProductStorage;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import java.net.URL;

public class NewProductStorageProvider extends
    FeedbackModuleProvider<NewProductStorage, ProductStorage> {

  private static final NewProductStorageProvider INSTANCE = new NewProductStorageProvider();
  private final URL fxml = getClass().getResource("/model/storage/NewProductStorage.fxml");

  private NewProductStorageProvider() {
  }

  public static NewProductStorageProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FeedbackModule<NewProductStorage, ProductStorage> create() {
    return new FeedbackModule<>(fxml) {
    };
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
