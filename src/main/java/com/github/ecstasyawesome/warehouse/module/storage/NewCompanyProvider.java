package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.NewCompany;
import com.github.ecstasyawesome.warehouse.core.FeedbackModule;
import com.github.ecstasyawesome.warehouse.core.FeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import java.net.URL;

public class NewCompanyProvider extends FeedbackModuleProvider<NewCompany, Company> {

  private static final NewCompanyProvider INSTANCE = new NewCompanyProvider();
  private final URL fxml = getClass().getResource("/model/storage/NewCompany.fxml");

  private NewCompanyProvider() {
  }

  public static NewCompanyProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FeedbackModule<NewCompany, Company> create() {
    return new FeedbackModule<>(fxml) {
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
