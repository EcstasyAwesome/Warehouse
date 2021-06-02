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
        & isFieldValid(identifierCodeField, NUMBERS, false)
        & isFieldValid(streetField, NAME, true) & isFieldValid(regionField, NAME, false)
        & isFieldValid(townField, NAME, false) & isFieldValid(numberField, WILDCARD, true)
        & isFieldValid(phoneField, PHONE, false) & isFieldValid(extraPhoneField, PHONE, true)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(siteField, URL, true)) {
      var companyCopy = new Company(company);
      var contact = company.getBusinessContact();
      var address = company.getAddress();
      company.setName(getFieldText(nameField));
      company.setIdentifierCode(getFieldText(identifierCodeField));
      contact.setPhone(getFieldText(phoneField));
      contact.setExtraPhone(getFieldText(extraPhoneField));
      contact.setEmail(getFieldText(emailField));
      contact.setSite(getFieldText(siteField));
      address.setRegion(getFieldText(regionField));
      address.setTown(getFieldText(townField));
      address.setStreet(getFieldText(streetField));
      address.setNumber(getFieldText(numberField));
      try {
        companyRepository.update(company);
        logger.info("Edited a company with id={}", company.getId());
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        company.setName(companyCopy.getName());
        company.setIdentifierCode(companyCopy.getIdentifierCode());
        contact.setPhone(companyCopy.getBusinessContact().getPhone());
        contact.setExtraPhone(companyCopy.getBusinessContact().getExtraPhone());
        contact.setEmail(companyCopy.getBusinessContact().getEmail());
        contact.setSite(companyCopy.getBusinessContact().getSite());
        address.setRegion(companyCopy.getAddress().getRegion());
        address.setTown(companyCopy.getAddress().getTown());
        address.setStreet(companyCopy.getAddress().getStreet());
        address.setNumber(companyCopy.getAddress().getNumber());
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public void apply(Company company) {
    this.company = company;
    personTypeChoiceBox.setValue(company.getPersonType());
    setFieldText(nameField, company.getName());
    setFieldText(identifierCodeField, company.getIdentifierCode());
    setFieldText(regionField, company.getAddress().getRegion());
    setFieldText(townField, company.getAddress().getTown());
    setFieldText(streetField, company.getAddress().getStreet());
    setFieldText(numberField, company.getAddress().getNumber());
    setFieldText(phoneField, company.getBusinessContact().getPhone());
    setFieldText(extraPhoneField, company.getBusinessContact().getExtraPhone());
    setFieldText(emailField, company.getBusinessContact().getEmail());
    setFieldText(siteField, company.getBusinessContact().getSite());
  }
}
