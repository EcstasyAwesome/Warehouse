package com.github.ecstasyawesome.warehouse.controller.storage;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductStorageRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CompanyRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductStorageRepositoryService;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditProductStorage extends ConfiguredFeedbackController<ProductStorage> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProductStorageRepository productStorageRepository =
      ProductStorageRepositoryService.getInstance();
  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(EditProductStorage.class);
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
  private void save(ActionEvent event) {
    if (isFieldValid(nameField, result.getName(), NAME, productStorageRepository)
        & isFieldValid(regionField, NAME, false) & isFieldValid(townField, NAME, false)
        & isFieldValid(streetField, NAME, true) & isFieldValid(numberField, WILDCARD, true)
        & isFieldValid(phoneField, PHONE, false) & isFieldValid(extraPhoneField, PHONE, true)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(siteField, URL, true)) {
      var storageCopy = new ProductStorage(result);
      var contact = result.getBusinessContact();
      var address = result.getAddress();
      result.setName(nameField.getText());
      contact.setPhone(phoneField.getText());
      contact.setExtraPhone(extraPhoneField.getText().isEmpty() ? null : extraPhoneField.getText());
      contact.setEmail(emailField.getText().isEmpty() ? null : emailField.getText());
      contact.setSite(siteField.getText().isEmpty() ? null : siteField.getText());
      address.setRegion(regionField.getText());
      address.setTown(townField.getText());
      address.setStreet(streetField.getText().isEmpty() ? null : streetField.getText());
      address.setNumber(numberField.getText().isEmpty() ? null : numberField.getText());
      try {
        productStorageRepository.update(result);
        logger.info("Edited a product storage with id={}", result.getId());
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        result.setName(storageCopy.getName());
        contact.setPhone(storageCopy.getBusinessContact().getPhone());
        contact.setExtraPhone(storageCopy.getBusinessContact().getExtraPhone());
        contact.setEmail(storageCopy.getBusinessContact().getEmail());
        contact.setSite(storageCopy.getBusinessContact().getSite());
        address.setRegion(storageCopy.getAddress().getRegion());
        address.setTown(storageCopy.getAddress().getTown());
        address.setStreet(storageCopy.getAddress().getStreet());
        address.setNumber(storageCopy.getAddress().getNumber());
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public void accept(ProductStorage productStorage) {
    result = productStorage;
    nameField.setText(productStorage.getName());
    companyChoiceBox.setValue(productStorage.getCompany());
    regionField.setText(productStorage.getAddress().getRegion());
    townField.setText(productStorage.getAddress().getTown());
    streetField.setText(Objects.requireNonNullElse(productStorage.getAddress().getStreet(), ""));
    numberField.setText(Objects.requireNonNullElse(productStorage.getAddress().getNumber(), ""));
    phoneField.setText(productStorage.getBusinessContact().getPhone());
    extraPhoneField.setText(Objects.requireNonNullElse(
        productStorage.getBusinessContact().getExtraPhone(), ""));
    emailField.setText(Objects.requireNonNullElse(
        productStorage.getBusinessContact().getEmail(), ""));
    siteField.setText(Objects.requireNonNullElse(
        productStorage.getBusinessContact().getSite(), ""));
  }

  @Override
  public ProductStorage get() {
    return result;
  }
}
