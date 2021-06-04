package com.github.ecstasyawesome.warehouse.controller.impl.provider;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ShowProductProviderController extends AbstractConfiguredController<ProductProvider> {

  @FXML
  private TextField nameField;

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
  public void apply(ProductProvider productProvider) {
    setFieldText(nameField, productProvider.getName());
    setFieldText(regionField, productProvider.getAddress().getRegion());
    setFieldText(townField, productProvider.getAddress().getTown());
    setFieldText(streetField, productProvider.getAddress().getStreet());
    setFieldText(numberField, productProvider.getAddress().getNumber());
    setFieldText(phoneField, productProvider.getBusinessContact().getPhone());
    setFieldText(extraPhoneField, productProvider.getBusinessContact().getExtraPhone());
    setFieldText(emailField, productProvider.getBusinessContact().getEmail());
    setFieldText(siteField, productProvider.getBusinessContact().getSite());
  }
}
