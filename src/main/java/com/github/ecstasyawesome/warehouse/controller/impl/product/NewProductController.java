package com.github.ecstasyawesome.warehouse.controller.impl.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.getFieldText;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.model.impl.Product.Builder;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CategoryRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductRepositoryService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewProductController extends AbstractFeedbackController<Product> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private final ProductRepository productRepository = ProductRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(NewProductController.class);
  private Product result;

  @FXML
  private TextField nameField;

  @FXML
  private ChoiceBox<Unit> unitChoiceBox;

  @FXML
  private ChoiceBox<Category> categoryChoiceBox;

  @FXML
  private void initialize() {
    unitChoiceBox.setItems(FXCollections.observableArrayList(Unit.values()));
    try {
      categoryChoiceBox.setItems(categoryRepository.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void add(ActionEvent event) {
    if (isFieldValid(nameField, null, NAME, productRepository)
        & isFieldValid(unitChoiceBox) & isFieldValid(categoryChoiceBox)) {
      var product = Builder.create()
          .setName(getFieldText(nameField))
          .setUnit(unitChoiceBox.getValue())
          .setCategory(categoryChoiceBox.getValue())
          .build();
      try {
        productRepository.create(product);
        logger.info("Added a product with id={}", product.getId());
        result = product;
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public Product get() {
    return result;
  }
}
