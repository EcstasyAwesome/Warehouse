package com.github.ecstasyawesome.warehouse.controller.impl.storage;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NUMBERS;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.getFieldText;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.PersonType;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CompanyRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditCompanyController extends AbstractConfiguredController<Company> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(EditCompanyController.class);
  private Company company;

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

  @FXML
  private void initialize() {
    personTypeChoiceBox.setItems(PersonType.getPersonTypes());
  }

  @FXML
  private void save(ActionEvent event) {
    if (isFieldValid(nameField, company.getName(), NAME, companyRepository)
        & isFieldValid(identifierCodeField, NUMBERS, false) & isFieldValid(personTypeChoiceBox)
        & isFieldValid(streetField, NAME, true) & isFieldValid(regionField, NAME, false)
        & isFieldValid(townField, NAME, false) & isFieldValid(numberField, WILDCARD, true)
        & isFieldValid(phoneField, PHONE, false) & isFieldValid(extraPhoneField, PHONE, true)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(siteField, URL, true)) {
      var companyCopy = new Company(company);
      company.setName(getFieldText(nameField));
      company.setPersonType(personTypeChoiceBox.getValue());
      company.setIdentifierCode(getFieldText(identifierCodeField));
      company.getBusinessContact().setPhone(getFieldText(phoneField));
      company.getBusinessContact().setExtraPhone(getFieldText(extraPhoneField));
      company.getBusinessContact().setEmail(getFieldText(emailField));
      company.getBusinessContact().setSite(getFieldText(siteField));
      company.getAddress().setRegion(getFieldText(regionField));
      company.getAddress().setTown(getFieldText(townField));
      company.getAddress().setStreet(getFieldText(streetField));
      company.getAddress().setNumber(getFieldText(numberField));
      if (!company.equals(companyCopy)) {
        try {
          companyRepository.update(company);
          logger.info("Edited a company with id={}", company.getId());
          closeCurrentStage(event);
        } catch (NullPointerException exception) {
          company.recover(companyCopy);
          windowManager.showDialog(exception);
        }
      }
    }
  }

  @Override
  public void apply(Company company) {
    this.company = company;
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
