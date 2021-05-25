package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.UserList;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class UserListProvider extends ModuleProvider<UserList> {

  private static final UserListProvider INSTANCE = new UserListProvider();
  public final URL fxml = getClass().getResource("/model/user/UserList.fxml");

  private UserListProvider() {
  }

  public static UserListProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public Module<UserList> create() {
    return new Module<>(fxml) {
    };
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
