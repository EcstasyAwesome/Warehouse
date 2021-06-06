package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.NewUserController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.core.module.AbstractFeedbackModule;
import java.net.URL;

public class NewUserModule extends AbstractFeedbackModule<NewUserController, User> {

  private static final NewUserModule INSTANCE = new NewUserModule();
  private final URL fxml = getClass().getResource("/model/user/NewUser.fxml");

  private NewUserModule() {
  }

  public static NewUserModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<NewUserController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "New user"; // TODO i18n
  }
}
