package com.github.ecstasyawesome.warehouse.provider.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.EditCompanyController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class EditCompanyProvider extends
    AbstractConfiguredModuleProvider<EditCompanyController, Company> {

  private static final EditCompanyProvider INSTANCE = new EditCompanyProvider();
  private final URL fxml = getClass().getResource("/model/storage/EditCompany.fxml");

  private EditCompanyProvider() {
  }

  public static EditCompanyProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<EditCompanyController> create() {
    return new FxmlModule<>(fxml);
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
