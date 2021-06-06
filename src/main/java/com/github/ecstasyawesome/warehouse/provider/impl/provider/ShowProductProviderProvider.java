package com.github.ecstasyawesome.warehouse.provider.impl.provider;

import com.github.ecstasyawesome.warehouse.controller.impl.provider.EditProductProviderController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class ShowProductProviderProvider extends
    AbstractConfiguredModuleProvider<EditProductProviderController, ProductProvider> {

  private static final ShowProductProviderProvider INSTANCE = new ShowProductProviderProvider();
  private final URL fxml = getClass().getResource("/model/provider/ShowProductProvider.fxml");

  private ShowProductProviderProvider() {
  }

  public static ShowProductProviderProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<EditProductProviderController> create() {
    return new FxmlModule<>(fxml);
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
