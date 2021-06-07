package com.github.ecstasyawesome.warehouse.controller.storage;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.model.impl.Address;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact.Builder;
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

public class NewProductStorageController extends AbstractFeedbackController<ProductStorage> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProductStorageRepository productStorageRepository =
      ProductStorageRepositoryService.getInstance();
  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(NewProductStorageController.class);
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
      try {
        var contact = Builder.create()
            .setPhone(getFieldText(phoneField))
            .setExtraPhone(getFieldText(extraPhoneField))
            .setEmail(getFieldText(emailField))
            .setSite(getFieldText(siteField))
            .build();
        var address = Address.Builder.create()
            .setRegion(getFieldText(regionField))
            .setTown(getFieldText(townField))
            .setStreet(getFieldText(streetField))
            .setNumber(getFieldText(numberField))
            .build();
        var productStorage = ProductStorage.Builder.create()
            .setName(getFieldText(nameField))
            .setCompany(companyChoiceBox.getValue())
            .setAddress(address)
            .setBusinessContact(contact)
            .build();
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
