package com.github.ecstasyawesome.warehouse.controller.impl.storage;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.model.PersonType;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ShowCompanyController extends AbstractConfiguredController<Company> {

  @FXML
  private TextField nameField;

  @FXML
  private ChoiceBox<PersonType> personTypeChoiceBox;

  @FXML
  private TextField identifierCodeField;

  @FXML
  private TextField regionField;

  @FXML
  private TextField townField;

  @FXML
  private TextField streetField;

  @FXML
  private TextField numberField;

  @FXML
  private TextField phoneField;

  @FXML
  private TextField extraPhoneField;

  @FXML
  private TextField emailField;

  @FXML
  private TextField siteField;

  @Override
  public void apply(Company company) {
    personTypeChoiceBox.setValue(company.getPersonType());
    setFieldText(nameField, company.getName());
    setFieldText(identifierCodeField, company.getIdentifierCode());
    setFieldText(townField, company.getAddress().getTown());
    setFieldText(regionField, company.getAddress().getRegion());
    setFieldText(streetField, company.getAddress().getStreet());
    setFieldText(phoneField, company.getBusinessContact().getPhone());
    setFieldText(numberField, company.getAddress().getNumber());
    setFieldText(extraPhoneField, company.getBusinessContact().getExtraPhone());
    setFieldText(emailField, company.getBusinessContact().getEmail());
    setFieldText(siteField, company.getBusinessContact().getSite());
  }
}
