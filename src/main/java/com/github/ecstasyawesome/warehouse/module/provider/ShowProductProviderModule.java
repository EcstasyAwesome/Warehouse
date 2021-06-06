package com.github.ecstasyawesome.warehouse.module.provider;

import com.github.ecstasyawesome.warehouse.controller.provider.EditProductProviderController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import java.net.URL;

public class ShowProductProviderModule extends
    AbstractConfiguredModule<EditProductProviderController, ProductProvider> {

  private static final ShowProductProviderModule INSTANCE = new ShowProductProviderModule();
  private final URL fxml = getClass().getResource("/model/provider/ShowProductProvider.fxml");

  private ShowProductProviderModule() {
  }

  public static ShowProductProviderModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<EditProductProviderController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Show product provider"; // TODO i18n
  }
}
