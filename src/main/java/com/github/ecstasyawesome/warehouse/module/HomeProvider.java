package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.Home;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class HomeProvider extends ModuleProvider<Home> {

  private static final HomeProvider INSTANCE = new HomeProvider();
  public final URL fxml = getClass().getResource("/model/Home.fxml");

  private HomeProvider() {
  }

  public static HomeProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public Module<Home> create() {
    return new Module<>(fxml) {
    };
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
