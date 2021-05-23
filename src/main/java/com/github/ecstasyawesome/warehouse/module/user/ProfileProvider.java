package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.Profile;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class ProfileProvider extends ModuleProvider<Profile> {

  public static final ProfileProvider INSTANCE = new ProfileProvider();
  public final URL fxml = getClass().getResource("/model/user/Profile.fxml");

  private ProfileProvider() {
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
