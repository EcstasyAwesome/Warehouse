package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.EditUserController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import java.net.URL;

public class EditUserModule extends AbstractConfiguredModule<EditUserController, User> {

  private static final EditUserModule INSTANCE = new EditUserModule();
  private final URL fxml = getClass().getResource("/model/user/EditUser.fxml");

  private EditUserModule() {
  }

  public static EditUserModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<EditUserController> create() {
    return new FxmlBundle<>(fxml);
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
