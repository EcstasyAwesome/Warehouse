package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.AdministratorRegistration;
import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleFactory;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleWrapper;
import java.net.URL;

public class AdministratorRegistrationModuleFactory
    implements FeedbackModuleFactory<AdministratorRegistration, String> {

  public static final AdministratorRegistrationModuleFactory INSTANCE =
      new AdministratorRegistrationModuleFactory();
  public final URL fxml = getClass().getResource("/model/AdministratorRegistration.fxml");

  private AdministratorRegistrationModuleFactory() {
  }

  @Override
  public FeedbackModuleWrapper<AdministratorRegistration, String> create() {
    return new FeedbackModuleWrapper<>("Registration", Access.GUEST, fxml) { // TODO i18n
    };
  }
}
