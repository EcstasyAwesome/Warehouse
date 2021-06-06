package com.github.ecstasyawesome.warehouse.provider.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.NewUserController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractFeedbackModuleProvider;
import java.net.URL;

public class NewUserProvider extends AbstractFeedbackModuleProvider<NewUserController, User> {

  private static final NewUserProvider INSTANCE = new NewUserProvider();
  private final URL fxml = getClass().getResource("/model/user/NewUser.fxml");

  private NewUserProvider() {
  }

  public static NewUserProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<NewUserController> create() {
    return new FxmlModule<>(fxml);
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
