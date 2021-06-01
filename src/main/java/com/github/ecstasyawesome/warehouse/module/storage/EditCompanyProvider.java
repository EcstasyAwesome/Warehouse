package com.github.ecstasyawesome.warehouse.module.storage;

import com.github.ecstasyawesome.warehouse.controller.storage.EditCompany;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import java.net.URL;

public class EditCompanyProvider extends ConfiguredFeedbackModuleProvider<EditCompany, Company> {

  private static final EditCompanyProvider INSTANCE = new EditCompanyProvider();
  private final URL fxml = getClass().getResource("/model/storage/EditCompany.fxml");

  private EditCompanyProvider() {
  }

  public static EditCompanyProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public ConfiguredFeedbackModule<EditCompany, Company> create() {
    return new ConfiguredFeedbackModule<>(fxml) {
    };
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
