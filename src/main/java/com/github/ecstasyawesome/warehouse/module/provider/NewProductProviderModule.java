package com.github.ecstasyawesome.warehouse.module.provider;

import com.github.ecstasyawesome.warehouse.controller.provider.NewProductProviderController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import java.net.URL;

public class NewProductProviderModule extends
    AbstractFeedbackModule<NewProductProviderController, ProductProvider> {

  private static final NewProductProviderModule INSTANCE = new NewProductProviderModule();
  private final URL fxml = getClass().getResource("/model/provider/NewProductProvider.fxml");

  private NewProductProviderModule() {
  }

  public static NewProductProviderModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<NewProductProviderController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "New product provider"; // TODO i18n
  }
}
