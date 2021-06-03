package com.github.ecstasyawesome.warehouse.controller.impl.storage;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ShowProductStorageController extends AbstractConfiguredController<ProductStorage> {

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

  @Override
  public void apply(ProductStorage productStorage) {
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
