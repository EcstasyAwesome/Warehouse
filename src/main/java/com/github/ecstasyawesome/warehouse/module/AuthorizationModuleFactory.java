package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.Authorization;
import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.ModuleFactory;
import com.github.ecstasyawesome.warehouse.core.ModuleWrapper;
import java.net.URL;

public class AuthorizationModuleFactory implements ModuleFactory<Authorization> {

  public static final AuthorizationModuleFactory INSTANCE = new AuthorizationModuleFactory();
  public final URL fxml = getClass().getResource("/model/Authorization.fxml");

  @Override
  public ModuleWrapper<Authorization> create() {
    return new ModuleWrapper<>("Authorization", Access.GUEST, fxml) { // TODO i18n
    };
  }
}
