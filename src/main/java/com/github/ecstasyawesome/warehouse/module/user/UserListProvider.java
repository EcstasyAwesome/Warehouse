package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.UserList;
import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import java.net.URL;

public class UserListProvider extends ModuleProvider<UserList> {

  public static final UserListProvider INSTANCE = new UserListProvider();
  public final URL fxml = getClass().getResource("/model/user/UserList.fxml");

  private UserListProvider() {
  }

  @Override
  public Module<UserList> create() {
    return new Module<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "User list"; // TODO i18n
  }
}
