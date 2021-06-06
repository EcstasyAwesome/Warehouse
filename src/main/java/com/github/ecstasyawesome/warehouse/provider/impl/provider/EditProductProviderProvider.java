package com.github.ecstasyawesome.warehouse.provider.impl.provider;

import com.github.ecstasyawesome.warehouse.controller.impl.provider.EditProductProviderController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class EditProductProviderProvider extends
    AbstractConfiguredModuleProvider<EditProductProviderController, ProductProvider> {

  private static final EditProductProviderProvider INSTANCE = new EditProductProviderProvider();
  private final URL fxml = getClass().getResource("/model/provider/EditProductProvider.fxml");

  private EditProductProviderProvider() {
  }

  public static EditProductProviderProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<EditProductProviderController> create() {
    return new FxmlModule<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "Edit product provider"; // TODO i18n
  }
}
