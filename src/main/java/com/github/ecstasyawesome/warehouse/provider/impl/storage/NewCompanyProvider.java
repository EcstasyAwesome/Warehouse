package com.github.ecstasyawesome.warehouse.provider.impl.storage;

import com.github.ecstasyawesome.warehouse.controller.impl.storage.NewCompanyController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractFeedbackModuleProvider;
import java.net.URL;

public class NewCompanyProvider extends
    AbstractFeedbackModuleProvider<NewCompanyController, Company> {

  private static final NewCompanyProvider INSTANCE = new NewCompanyProvider();
  private final URL fxml = getClass().getResource("/model/storage/NewCompany.fxml");

  private NewCompanyProvider() {
  }

  public static NewCompanyProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractFeedbackModule<NewCompanyController, Company> create() {
    return new AbstractFeedbackModule<>(fxml) {
    };
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
