package com.github.ecstasyawesome.warehouse.controller.impl.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.getFieldText;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CategoryRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductRepositoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditProductController extends AbstractConfiguredController<Product> {

  private final ProductRepository productRepository = ProductRepositoryService.getInstance();
  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(EditProductController.class);
  private Product product;

  @FXML
  private TextField nameField;

  @FXML
  private ChoiceBox<Unit> unitChoiceBox;

  @FXML
  private ChoiceBox<Category> categoryChoiceBox;

  @FXML
  private void initialize() {
    try {
      categoryChoiceBox.setItems(categoryRepository.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void save(ActionEvent event) {
    if (isFieldValid(nameField, product.getName(), NAME, productRepository)
        & isFieldValid(categoryChoiceBox)) {
      var productCopy = new Product(product);
      product.setName(getFieldText(nameField));
      product.setCategory(categoryChoiceBox.getValue());
      try {
        productRepository.update(product);
        logger.info("Edited a product with id={}", product.getId());
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        product.recover(productCopy);
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public void apply(Product product) {
    this.product = product;
    setFieldText(nameField, product.getName());
    unitChoiceBox.setValue(product.getUnit());
    categoryChoiceBox.setValue(product.getCategory());
  }
}
