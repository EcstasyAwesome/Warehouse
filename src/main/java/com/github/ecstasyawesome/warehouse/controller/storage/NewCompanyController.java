package com.github.ecstasyawesome.warehouse.controller.storage;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NUMBERS;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.model.PersonType;
import com.github.ecstasyawesome.warehouse.model.impl.Address.Builder;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CompanyRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewCompanyController extends AbstractFeedbackController<Company> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(NewCompanyController.class);
  private Company result;

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
  private void add(ActionEvent event) {
    if (isFieldValid(nameField, null, NAME, companyRepository)
        & isFieldValid(personTypeChoiceBox) & isFieldValid(identifierCodeField, NUMBERS, false)
        & isFieldValid(regionField, NAME, false) & isFieldValid(townField, NAME, false)
        & isFieldValid(streetField, NAME, true) & isFieldValid(numberField, WILDCARD, true)
        & isFieldValid(phoneField, PHONE, false) & isFieldValid(extraPhoneField, PHONE, true)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(siteField, URL, true)) {
      try {
        var address = Builder.create()
            .setRegion(getFieldText(regionField))
            .setTown(getFieldText(townField))
            .setStreet(getFieldText(streetField))
            .setNumber(getFieldText(numberField))
            .build();
        var contact = BusinessContact.Builder.create()
            .setPhone(getFieldText(phoneField))
            .setExtraPhone(getFieldText(extraPhoneField))
            .setEmail(getFieldText(emailField))
            .setSite(getFieldText(siteField))
            .build();
        var company = Company.Builder.create()
            .setName(getFieldText(nameField))
            .setPersonType(personTypeChoiceBox.getValue())
            .setIdentifierCode(getFieldText(identifierCodeField))
            .setAddress(address)
            .setBusinessContact(contact)
            .build();
        companyRepository.create(company);
        logger.info("Added a company with id={}", company.getId());
        result = company;
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public Company get() {
    return result;
  }
}
