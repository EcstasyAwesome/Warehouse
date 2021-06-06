package com.github.ecstasyawesome.warehouse.provider.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.ProfileController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import java.net.URL;

public class ProfileProvider extends AbstractModuleProvider<ProfileController> {

  private static final ProfileProvider INSTANCE = new ProfileProvider();
  private final URL fxml = getClass().getResource("/model/user/Profile.fxml");

  private ProfileProvider() {
  }

  public static ProfileProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<ProfileController> create() {
    return new FxmlModule<>(fxml);
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
