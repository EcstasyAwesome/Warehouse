package com.github.ecstasyawesome.warehouse.module.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.AuthorizationController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import java.net.URL;

public class AuthorizationModule extends AbstractModule<AuthorizationController> {

  private static final AuthorizationModule INSTANCE = new AuthorizationModule();
  private final URL fxml = getClass().getResource("/model/user/Authorization.fxml");

  private AuthorizationModule() {
  }

  public static AuthorizationModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<AuthorizationController> create() {
    return new FxmlBundle<>(fxml);
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
