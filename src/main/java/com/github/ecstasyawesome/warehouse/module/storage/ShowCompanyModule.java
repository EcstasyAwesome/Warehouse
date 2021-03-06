package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.EditCompanyController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import java.net.URL;

public class ShowCompanyModule extends AbstractConfiguredModule<EditCompanyController, Company> {

  private static final ShowCompanyModule INSTANCE = new ShowCompanyModule();
  private final URL fxml = getClass().getResource("/model/storage/ShowCompany.fxml");

  private ShowCompanyModule() {
  }

  public static ShowCompanyModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<EditCompanyController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.ADMIN;
  }

  @Override
  public String getTitle() {
    return "Show company"; // TODO i18n
  }
}
