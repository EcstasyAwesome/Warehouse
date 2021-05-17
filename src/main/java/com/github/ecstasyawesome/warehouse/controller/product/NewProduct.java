package com.github.ecstasyawesome.warehouse.controller.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.FeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.CategoryDao;
import com.github.ecstasyawesome.warehouse.dao.ProductDao;
import com.github.ecstasyawesome.warehouse.dao.impl.CategoryDaoService;
import com.github.ecstasyawesome.warehouse.dao.impl.ProductDaoService;
import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import com.github.ecstasyawesome.warehouse.model.Unit;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewProduct extends FeedbackController<Product> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CategoryDao categoryDao = CategoryDaoService.getInstance();
  private final ProductDao productDao = ProductDaoService.getInstance();
  private final Logger logger = LogManager.getLogger(NewProduct.class);
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
      categoryChoiceBox.setItems(FXCollections.observableArrayList(categoryDao.getAll()));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void add(ActionEvent event) {
    if (isFieldValid(nameField, NAME, productDao)
        & isFieldValid(unitChoiceBox) & isFieldValid(categoryChoiceBox)) {
      var product = Product.builder()
          .name(nameField.getText())
          .unit(unitChoiceBox.getValue())
          .category(categoryChoiceBox.getValue())
          .build();
      try {
        var id = productDao.create(product);
        product.setId(id);
        logger.info("Added a product with id={}", id);
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
