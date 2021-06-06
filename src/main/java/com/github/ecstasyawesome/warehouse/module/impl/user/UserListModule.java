package com.github.ecstasyawesome.warehouse.module.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.UserListController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import java.net.URL;

public class UserListModule extends AbstractModule<UserListController> {

  private static final UserListModule INSTANCE = new UserListModule();
  private final URL fxml = getClass().getResource("/model/user/UserList.fxml");

  private UserListModule() {
  }

  public static UserListModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<UserListController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "User list"; // TODO i18n
  }
}
