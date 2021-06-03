package com.github.ecstasyawesome.warehouse.provider.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.EditCompanyController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class ShowCompanyProvider extends
    AbstractConfiguredModuleProvider<EditCompanyController, Company> {

  private static final ShowCompanyProvider INSTANCE = new ShowCompanyProvider();
  private final URL fxml = getClass().getResource("/model/storage/ShowCompany.fxml");

  private ShowCompanyProvider() {
  }

  public static ShowCompanyProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractConfiguredModule<EditCompanyController, Company> create() {
    return new AbstractConfiguredModule<>(fxml) {
    };
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
