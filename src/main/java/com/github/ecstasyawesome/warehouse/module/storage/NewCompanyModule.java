package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.NewCompanyController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import java.net.URL;

public class NewCompanyModule extends AbstractFeedbackModule<NewCompanyController, Company> {

  private static final NewCompanyModule INSTANCE = new NewCompanyModule();
  private final URL fxml = getClass().getResource("/model/storage/NewCompany.fxml");

  private NewCompanyModule() {
  }

  public static NewCompanyModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<NewCompanyController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "New company"; // TODO i18n
  }
}
