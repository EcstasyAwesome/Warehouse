package com.github.ecstasyawesome.warehouse.module.provider;

import com.github.ecstasyawesome.warehouse.controller.provider.EditProductProviderController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import java.net.URL;

public class EditProductProviderModule extends
    AbstractConfiguredModule<EditProductProviderController, ProductProvider> {

  private static final EditProductProviderModule INSTANCE = new EditProductProviderModule();
  private final URL fxml = getClass().getResource("/model/provider/EditProductProvider.fxml");

  private EditProductProviderModule() {
  }

  public static EditProductProviderModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<EditProductProviderController> create() {
    return new FxmlBundle<>(fxml);
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
