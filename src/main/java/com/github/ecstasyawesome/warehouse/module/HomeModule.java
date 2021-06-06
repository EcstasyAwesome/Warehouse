package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.HomeController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class HomeModule extends AbstractModule<HomeController> {

  private static final HomeModule INSTANCE = new HomeModule();
  private final URL fxml = getClass().getResource("/model/Home.fxml");

  private HomeModule() {
  }

  public static HomeModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<HomeController> create() {
    return new FxmlBundle<>(fxml);
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
