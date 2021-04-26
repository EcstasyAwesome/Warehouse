package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.AdministratorRegistration;
import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import java.net.URL;

public class AdministratorRegistrationProvider
    extends FeedbackModuleProvider<AdministratorRegistration, String> {

  public static final AdministratorRegistrationProvider INSTANCE =
      new AdministratorRegistrationProvider();
  public final URL fxml = getClass().getResource("/model/AdministratorRegistration.fxml");

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
