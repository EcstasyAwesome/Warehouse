package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.NewUser;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import java.net.URL;

public class NewUserProvider extends FeedbackModuleProvider<NewUser, User> {

  private static final NewUserProvider INSTANCE = new NewUserProvider();
  private final URL fxml = getClass().getResource("/model/user/NewUser.fxml");

  private NewUserProvider() {
  }

  public static NewUserProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FeedbackModule<NewUser, User> create() {
    return new FeedbackModule<>(fxml) {
    };
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
