package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.AdministratorRegistration;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.UserSecurity;
import java.net.URL;

public class AdministratorRegistrationProvider
    extends FeedbackModuleProvider<AdministratorRegistration, UserSecurity> {

  public static final AdministratorRegistrationProvider INSTANCE =
      new AdministratorRegistrationProvider();
  public final URL fxml = getClass().getResource("/model/user/AdministratorRegistration.fxml");

  private AdministratorRegistrationProvider() {
  }

  @Override
  public FeedbackModule<AdministratorRegistration, UserSecurity> create() {
    return new FeedbackModule<>(fxml) {
    };
  }

  @Override
  public Access getAccess() {
    return Access.GUEST;
  }

  @Override
  public String getTitle() {
    return "Registration"; // TODO i18n
  }
}
