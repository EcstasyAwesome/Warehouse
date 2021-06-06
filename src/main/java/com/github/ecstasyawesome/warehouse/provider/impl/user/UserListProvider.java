package com.github.ecstasyawesome.warehouse.provider.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.UserListController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import java.net.URL;

public class UserListProvider extends AbstractModuleProvider<UserListController> {

  private static final UserListProvider INSTANCE = new UserListProvider();
  private final URL fxml = getClass().getResource("/model/user/UserList.fxml");

  private UserListProvider() {
  }

  public static UserListProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<UserListController> create() {
    return new FxmlModule<>(fxml);
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
