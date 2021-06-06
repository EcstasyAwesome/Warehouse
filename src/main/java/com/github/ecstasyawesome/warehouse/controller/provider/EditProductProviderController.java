package com.github.ecstasyawesome.warehouse.controller.provider;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.getFieldText;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.repository.ProductProviderRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductProviderRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditProductProviderController extends AbstractConfiguredController<ProductProvider> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProductProviderRepository productProviderRepository =
      ProductProviderRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(EditProductProviderController.class);
  private ProductProvider productProvider;

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

  @FXML
  private void save(ActionEvent event) {
    if (isFieldValid(nameField, productProvider.getName(), NAME, productProviderRepository)
        & isFieldValid(regionField, NAME, false) & isFieldValid(townField, NAME, false)
        & isFieldValid(streetField, NAME, true) & isFieldValid(numberField, WILDCARD, true)
        & isFieldValid(phoneField, PHONE, false) & isFieldValid(extraPhoneField, PHONE, true)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(siteField, URL, true)) {
      var providerCopy = new ProductProvider(productProvider);
      productProvider.setName(getFieldText(nameField));
      productProvider.getBusinessContact().setPhone(getFieldText(phoneField));
      productProvider.getBusinessContact().setExtraPhone(getFieldText(extraPhoneField));
      productProvider.getBusinessContact().setEmail(getFieldText(emailField));
      productProvider.getBusinessContact().setSite(getFieldText(siteField));
      productProvider.getAddress().setTown(getFieldText(townField));
      productProvider.getAddress().setRegion(getFieldText(regionField));
      productProvider.getAddress().setStreet(getFieldText(streetField));
      productProvider.getAddress().setNumber(getFieldText(numberField));
      if (!productProvider.equals(providerCopy)) {
        try {
          productProviderRepository.update(productProvider);
          logger.info("Edited a product provider with id={}", productProvider.getId());
          closeCurrentStage(event);
        } catch (NullPointerException exception) {
          productProvider.recover(providerCopy);
          windowManager.showDialog(exception);
        }
      }
    }
  }

  @Override
  public void apply(ProductProvider productProvider) {
    this.productProvider = productProvider;
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
