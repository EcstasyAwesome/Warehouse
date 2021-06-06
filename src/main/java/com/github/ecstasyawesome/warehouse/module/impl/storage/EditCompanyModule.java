package com.github.ecstasyawesome.warehouse.module.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.EditCompanyController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import java.net.URL;

public class EditCompanyModule extends
    AbstractConfiguredModule<EditCompanyController, Company> {

  private static final EditCompanyModule INSTANCE = new EditCompanyModule();
  private final URL fxml = getClass().getResource("/model/storage/EditCompany.fxml");

  private EditCompanyModule() {
  }

  public static EditCompanyModule getInstance() {
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
    return "Edit company"; // TODO i18n
  }
}
