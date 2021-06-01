package com.github.ecstasyawesome.warehouse.provider.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.AdministratorController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.PersonSecurity;
import com.github.ecstasyawesome.warehouse.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractFeedbackModuleProvider;
import java.net.URL;

public class AdministratorRegistrationProvider
    extends AbstractFeedbackModuleProvider<AdministratorController, PersonSecurity> {

  private static final AdministratorRegistrationProvider INSTANCE =
      new AdministratorRegistrationProvider();
  private final URL fxml = getClass().getResource("/model/user/Administrator.fxml");

  private AdministratorRegistrationProvider() {
  }

  public static AdministratorRegistrationProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractFeedbackModule<AdministratorController, PersonSecurity> create() {
    return new AbstractFeedbackModule<>(fxml) {
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
