package com.github.ecstasyawesome.warehouse.provider.impl;

import com.github.ecstasyawesome.warehouse.controller.impl.HomeController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import java.net.URL;

public class HomeProvider extends AbstractModuleProvider<HomeController> {

  private static final HomeProvider INSTANCE = new HomeProvider();
  private final URL fxml = getClass().getResource("/model/Home.fxml");

  private HomeProvider() {
  }

  public static HomeProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<HomeController> create() {
    return new FxmlModule<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Home"; // TODO i18n
  }
}
