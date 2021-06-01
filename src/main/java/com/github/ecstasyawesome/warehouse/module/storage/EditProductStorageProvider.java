package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.EditProductStorage;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import java.net.URL;

public class EditProductStorageProvider extends
    ConfiguredFeedbackModuleProvider<EditProductStorage, ProductStorage> {

  private static final EditProductStorageProvider INSTANCE = new EditProductStorageProvider();
  private final URL fxml = getClass().getResource("/model/storage/EditProductStorage.fxml");

  private EditProductStorageProvider() {
  }

  public static EditProductStorageProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public ConfiguredFeedbackModule<EditProductStorage, ProductStorage> create() {
    return new ConfiguredFeedbackModule<>(fxml) {
    };
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
