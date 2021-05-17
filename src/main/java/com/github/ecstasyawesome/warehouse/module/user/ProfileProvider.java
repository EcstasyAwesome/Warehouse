package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.Profile;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.User;
import java.net.URL;

public class ProfileProvider extends ConfiguredFeedbackModuleProvider<Profile, User> {

  public static final ProfileProvider INSTANCE = new ProfileProvider();
  public final URL fxml = getClass().getResource("/model/user/Profile.fxml");

  private ProfileProvider() {
  }

  @Override
  public ConfiguredFeedbackModule<Profile, User> create() {
    return new ConfiguredFeedbackModule<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Profile"; // TODO i18n
  }
}
