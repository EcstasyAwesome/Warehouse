package com.github.ecstasyawesome.warehouse.controller.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import com.github.ecstasyawesome.warehouse.model.Unit;
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

public class EditProduct extends ConfiguredFeedbackController<Product> {

  private final ProductRepository productRepository = ProductRepositoryService.getInstance();
  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(EditProduct.class);
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
      product.setName(nameField.getText());
      product.setCategory(categoryChoiceBox.getValue());
      try {
        productRepository.update(product);
        logger.info("Edited a product with id={}", product.getId());
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        product.setName(productCopy.getName());
        product.setCategory(productCopy.getCategory());
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public void accept(Product product) {
    this.product = product;
    nameField.setText(product.getName());
    unitChoiceBox.setValue(product.getUnit());
    categoryChoiceBox.setValue(product.getCategory());
  }

  @Override
  public Product get() {
    return product;
  }
}
