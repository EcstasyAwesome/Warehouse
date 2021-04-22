package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.Home;
import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.ModuleFactory;
import com.github.ecstasyawesome.warehouse.core.ModuleWrapper;
import java.net.URL;

public class HomeModuleFactory implements ModuleFactory<Home> {

  public static final HomeModuleFactory INSTANCE = new HomeModuleFactory();
  public final URL fxml = getClass().getResource("/model/Home.fxml");

  private HomeModuleFactory() {
  }

  @Override
  public ModuleWrapper<Home> create() {
    return new ModuleWrapper<>("Home", Access.USER, fxml) { // TODO i18n
    };
  }
}
