package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.ProfileController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class ProfileModule extends AbstractModule<ProfileController> {

  private static final ProfileModule INSTANCE = new ProfileModule();
  private final URL fxml = getClass().getResource("/model/user/Profile.fxml");

  private ProfileModule() {
  }

  public static ProfileModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ProfileController> create() {
    return new FxmlBundle<>(fxml);
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
