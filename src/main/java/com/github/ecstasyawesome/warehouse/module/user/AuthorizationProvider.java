package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.Authorization;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class AuthorizationProvider extends ModuleProvider<Authorization> {

  public static final AuthorizationProvider INSTANCE = new AuthorizationProvider();
  public final URL fxml = getClass().getResource("/model/user/Authorization.fxml");

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
