package com.github.ecstasyawesome.warehouse.provider.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.AuthorizationController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import java.net.URL;

public class AuthorizationProvider extends AbstractModuleProvider<AuthorizationController> {

  private static final AuthorizationProvider INSTANCE = new AuthorizationProvider();
  private final URL fxml = getClass().getResource("/model/user/Authorization.fxml");

  private AuthorizationProvider() {
  }

  public static AuthorizationProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<AuthorizationController> create() {
    return new FxmlModule<>(fxml);
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
