package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.AdministratorRegistration;
import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import java.net.URL;

public class AdministratorRegistrationProvider
    extends FeedbackModuleProvider<AdministratorRegistration, String> {

  public static final AdministratorRegistrationProvider INSTANCE =
      new AdministratorRegistrationProvider();
  public final URL fxml = getClass().getResource("/model/user/AdministratorRegistration.fxml");

  private AdministratorRegistrationProvider() {
  }

  @Override
  public FeedbackModule<AdministratorRegistration, String> create() {
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
