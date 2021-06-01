package com.github.ecstasyawesome.warehouse.provider.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.EditUserController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class EditUserProvider extends AbstractConfiguredModuleProvider<EditUserController, User> {

  private static final EditUserProvider INSTANCE = new EditUserProvider();
  private final URL fxml = getClass().getResource("/model/user/EditUser.fxml");

  private EditUserProvider() {
  }

  public static EditUserProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractConfiguredModule<EditUserController, User> create() {
    return new AbstractConfiguredModule<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "Edit user profile"; //TODO i18n
  }
}
