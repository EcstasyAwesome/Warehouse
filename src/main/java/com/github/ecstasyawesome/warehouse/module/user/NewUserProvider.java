package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.NewUser;
import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.User;
import java.net.URL;

public class NewUserProvider extends FeedbackModuleProvider<NewUser, User> {

  public static final NewUserProvider INSTANCE = new NewUserProvider();
  public final URL fxml = getClass().getResource("/model/user/NewUser.fxml");

  private NewUserProvider() {
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
