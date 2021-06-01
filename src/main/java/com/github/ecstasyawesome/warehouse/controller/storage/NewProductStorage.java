package com.github.ecstasyawesome.warehouse.controller.storage;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.FeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.impl.Address;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductStorageRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CompanyRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductStorageRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewProductStorage extends FeedbackController<ProductStorage> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProductStorageRepository productStorageRepository =
      ProductStorageRepositoryService.getInstance();
  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(NewCompany.class);
  private ProductStorage result;

  @FXML
  private TextField nameField;

  @FXML
  private ChoiceBox<Company> companyChoiceBox;

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
    try {
      companyChoiceBox.setItems(companyRepository.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void add(ActionEvent event) {
    if (isFieldValid(nameField, null, NAME, productStorageRepository)
        & isFieldValid(companyChoiceBox) & isFieldValid(regionField, NAME, false)
        & isFieldValid(townField, NAME, false) & isFieldValid(streetField, NAME, true)
        & isFieldValid(numberField, WILDCARD, true) & isFieldValid(phoneField, PHONE, false)
        & isFieldValid(extraPhoneField, PHONE, true) & isFieldValid(emailField, EMAIL, true)
        & isFieldValid(siteField, URL, true)) {
      var contact = BusinessContact.Builder.create()
          .setPhone(phoneField.getText())
          .setExtraPhone(extraPhoneField.getText().isEmpty() ? null : extraPhoneField.getText())
          .setEmail(emailField.getText().isEmpty() ? null : emailField.getText())
          .setSite(siteField.getText().isEmpty() ? null : siteField.getText())
          .build();
      var address = Address.Builder.create()
          .setRegion(regionField.getText())
          .setTown(townField.getText())
          .setStreet(streetField.getText().isEmpty() ? null : streetField.getText())
          .setNumber(numberField.getText().isEmpty() ? null : numberField.getText())
          .build();
      var productStorage = ProductStorage.Builder.create()
          .setName(nameField.getText())
          .setCompany(companyChoiceBox.getValue())
          .setAddress(address)
          .setBusinessContact(contact)
          .build();
      try {
        productStorageRepository.create(productStorage);
        logger.info("Added a product storage with id={}", productStorage.getId());
        result = productStorage;
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public ProductStorage get() {
    return result;
  }
}
