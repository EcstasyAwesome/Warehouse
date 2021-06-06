package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.AdministratorController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.PersonSecurity;
import com.github.ecstasyawesome.warehouse.core.module.AbstractFeedbackModule;
import java.net.URL;

public class AdministratorRegistrationModule
    extends AbstractFeedbackModule<AdministratorController, PersonSecurity> {

  private static final AdministratorRegistrationModule INSTANCE =
      new AdministratorRegistrationModule();
  private final URL fxml = getClass().getResource("/model/user/Administrator.fxml");

  private AdministratorRegistrationModule() {
  }

  public static AdministratorRegistrationModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<AdministratorController> create() {
    return new FxmlBundle<>(fxml);
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
