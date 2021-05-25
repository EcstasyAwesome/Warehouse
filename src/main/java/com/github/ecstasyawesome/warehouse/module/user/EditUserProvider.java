package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.EditUser;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.User;
import java.net.URL;

public class EditUserProvider extends ConfiguredFeedbackModuleProvider<EditUser, User> {

  private static final EditUserProvider INSTANCE = new EditUserProvider();
  private final URL fxml = getClass().getResource("/model/user/EditUser.fxml");

  private EditUserProvider() {
  }

  public static EditUserProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public ConfiguredFeedbackModule<EditUser, User> create() {
    return new ConfiguredFeedbackModule<>(fxml) {
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
