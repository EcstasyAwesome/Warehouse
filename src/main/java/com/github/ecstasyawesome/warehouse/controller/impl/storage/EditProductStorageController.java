package com.github.ecstasyawesome.warehouse.controller.impl.storage;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.getFieldText;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
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

public class EditProductStorageController extends AbstractConfiguredController<ProductStorage> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProductStorageRepository productStorageRepository =
      ProductStorageRepositoryService.getInstance();
  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(EditProductStorageController.class);
  private ProductStorage productStorage;

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
    if (isFieldValid(nameField, productStorage.getName(), NAME, productStorageRepository)
        & isFieldValid(regionField, NAME, false) & isFieldValid(townField, NAME, false)
        & isFieldValid(streetField, NAME, true) & isFieldValid(numberField, WILDCARD, true)
        & isFieldValid(phoneField, PHONE, false) & isFieldValid(extraPhoneField, PHONE, true)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(siteField, URL, true)) {
      var storageCopy = new ProductStorage(productStorage);
      var contact = productStorage.getBusinessContact();
      var address = productStorage.getAddress();
      productStorage.setName(getFieldText(nameField));
      contact.setPhone(getFieldText(phoneField));
      contact.setExtraPhone(getFieldText(extraPhoneField));
      contact.setEmail(getFieldText(emailField));
      contact.setSite(getFieldText(siteField));
      address.setRegion(getFieldText(regionField));
      address.setTown(getFieldText(townField));
      address.setStreet(getFieldText(streetField));
      address.setNumber(getFieldText(numberField));
      try {
        productStorageRepository.update(productStorage);
        logger.info("Edited a product storage with id={}", productStorage.getId());
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        productStorage.setName(storageCopy.getName());
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
  public void apply(ProductStorage productStorage) {
    this.productStorage = productStorage;
    companyChoiceBox.setValue(productStorage.getCompany());
    setFieldText(nameField, productStorage.getName());
    setFieldText(regionField, productStorage.getAddress().getRegion());
    setFieldText(townField, productStorage.getAddress().getTown());
    setFieldText(streetField, productStorage.getAddress().getStreet());
    setFieldText(numberField, productStorage.getAddress().getNumber());
    setFieldText(phoneField, productStorage.getBusinessContact().getPhone());
    setFieldText(extraPhoneField, productStorage.getBusinessContact().getExtraPhone());
    setFieldText(emailField, productStorage.getBusinessContact().getEmail());
    setFieldText(siteField, productStorage.getBusinessContact().getSite());
  }
}
