package com.github.ecstasyawesome.warehouse.provider.impl.provider;

import com.github.ecstasyawesome.warehouse.controller.impl.provider.NewProductProviderController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractFeedbackModuleProvider;
import java.net.URL;

public class NewProductProviderProvider extends
    AbstractFeedbackModuleProvider<NewProductProviderController, ProductProvider> {

  private static final NewProductProviderProvider INSTANCE = new NewProductProviderProvider();
  private final URL fxml = getClass().getResource("/model/provider/NewProductProvider.fxml");

  private NewProductProviderProvider() {
  }

  public static NewProductProviderProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractFeedbackModule<NewProductProviderController, ProductProvider> create() {
    return new AbstractFeedbackModule<>(fxml) {
    };
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
