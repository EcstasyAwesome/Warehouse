package com.github.ecstasyawesome.warehouse.provider.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.EditProductStorageController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class EditProductStorageProvider extends
    AbstractConfiguredModuleProvider<EditProductStorageController, ProductStorage> {

  private static final EditProductStorageProvider INSTANCE = new EditProductStorageProvider();
  private final URL fxml = getClass().getResource("/model/storage/EditProductStorage.fxml");

  private EditProductStorageProvider() {
  }

  public static EditProductStorageProvider getInstance() {
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
    return "Edit product storage"; // TODO i18n
  }
}
