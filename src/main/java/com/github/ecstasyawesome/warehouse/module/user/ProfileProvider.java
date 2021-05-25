package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.Profile;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class ProfileProvider extends ModuleProvider<Profile> {

  private static final ProfileProvider INSTANCE = new ProfileProvider();
  private final URL fxml = getClass().getResource("/model/user/Profile.fxml");

  private ProfileProvider() {
  }

  public static ProfileProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public Module<Profile> create() {
    return new Module<>(fxml) {
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
