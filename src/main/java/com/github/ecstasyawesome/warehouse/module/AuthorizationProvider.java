package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.Authorization;
import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import java.net.URL;

public class AuthorizationProvider extends ModuleProvider<Authorization> {

  public static final AuthorizationProvider INSTANCE = new AuthorizationProvider();
  public final URL fxml = getClass().getResource("/model/Authorization.fxml");

  private AuthorizationProvider() {
  }

  @Override
  public Module<Authorization> create() {
    return new Module<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.GUEST;
  }

  @Override
  public String getTitle() {
    return "Authorization"; // TODO i18n
  }
}
