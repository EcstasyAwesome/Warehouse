package com.github.ecstasyawesome.warehouse.controller.provider;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.URL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.getFieldText;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.impl.Address;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact.Builder;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.repository.ProductProviderRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductProviderRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewProductProviderController extends AbstractFeedbackController<ProductProvider> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProductProviderRepository productProviderRepository =
      ProductProviderRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(NewProductProviderController.class);
  private ProductProvider result;

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
  private void add(ActionEvent event) {
    if (isFieldValid(nameField, null, NAME, productProviderRepository)
        & isFieldValid(regionField, NAME, false) & isFieldValid(townField, NAME, false)
        & isFieldValid(streetField, NAME, true) & isFieldValid(numberField, WILDCARD, true)
        & isFieldValid(phoneField, PHONE, false) & isFieldValid(extraPhoneField, PHONE, true)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(siteField, URL, true)) {
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
        var productProvider = ProductProvider.Builder.create()
            .setName(getFieldText(nameField))
            .setAddress(address)
            .setBusinessContact(contact)
            .build();
        productProviderRepository.create(productProvider);
        logger.info("Added a product provider with id={}", productProvider.getId());
        result = productProvider;
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public ProductProvider get() {
    return result;
  }
}
