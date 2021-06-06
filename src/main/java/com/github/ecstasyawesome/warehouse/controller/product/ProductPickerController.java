package com.github.ecstasyawesome.warehouse.controller.product;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CategoryRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductRepositoryService;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductPickerController extends AbstractFeedbackController<HashSet<Product>> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private final ProductRepository productRepository = ProductRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(ProductPickerController.class);
  private final HashSet<Product> pickedProducts = new LinkedHashSet<>();
  private final HashSet<Product> result = new LinkedHashSet<>();

  @FXML
  private Button resetButton;

  @FXML
  private Button pickButton;

  @FXML
  private Button pickSelectedButton;

  @FXML
  private TextField searchField;

  @FXML
  private Label pickedLabel;

  @FXML
  private TableView<Category> categoryTable;

  @FXML
  private TableColumn<Category, String> categoryNameColumn;

  @FXML
  private TableView<Product> productTable;

  @FXML
  private TableColumn<Product, String> productNameColumn;

  @FXML
  private TableColumn<Product, Unit> productUnitColumn;

  @FXML
  private TableColumn<Product, Category> productCategoryColumn;

  @FXML
  private TableColumn<Product, Boolean> productChoiceColumn;

  @FXML
  private void initialize() {
    categoryNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());
    productNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());
    productUnitColumn.setCellValueFactory(entry -> entry.getValue().unitProperty());
    productCategoryColumn.setCellValueFactory(entry -> entry.getValue().categoryProperty());
    productChoiceColumn.setCellFactory(factory -> new CheckBoxTableCell<>());
    productChoiceColumn.setCellValueFactory(cellData -> {
      var product = cellData.getValue();
      var property = new SimpleBooleanProperty(pickedProducts.contains(product));
      property.addListener((observable, oldValue, newValue) -> {
        if (newValue) {
          pickedProducts.add(product);
          logger.debug("Added to list a product with id={}", product.getId());
        } else {
          pickedProducts.remove(product);
          logger.debug("Removed from list a product with id={}", product.getId());
        }
        var size = pickedProducts.size();
        pickedLabel.setText(String.valueOf(size));
        pickSelectedButton.setDisable(size == 0);
      });
      return property;
    });

    categoryTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevCategory, currentCategory) -> {
          if (currentCategory != null) {
            getProductsFromDatabase(currentCategory);
            resetButton.setDisable(false);
            searchField.clear();
          }
        });

    productTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevProduct, currentProduct) -> {
          pickButton.setDisable(currentProduct == null);
        });

    getCategoriesFromDatabase();
  }

  @FXML
  private void clear() {
    resetButton.setDisable(true);
    searchField.clear();
    categoryTable.getSelectionModel().clearSelection();
    productTable.getItems().clear();
  }

  @FXML
  private void pick(Event event) {
    var selectionModel = productTable.getSelectionModel();
    if (!selectionModel.isEmpty()) {
      var product = selectionModel.getSelectedItem();
      result.add(product);
      logger.debug("Picked a product with id={}", product.getId());
      closeCurrentStage(event);
    }
  }

  @FXML
  private void pickSelected(ActionEvent event) {
    result.addAll(pickedProducts);
    logger.debug("Total picked {} product(s)", pickedProducts.size());
    closeCurrentStage(event);
  }

  @FXML
  private void search() {
    resetButton.setDisable(false);
    categoryTable.getSelectionModel().clearSelection();
    try {
      productTable.setItems(productRepository.search(searchField.getText()));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void doOnSortProductTable() {
    productTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void doOnKeyReleasedOnProductTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      pick(event);
    }
  }

  @FXML
  private void doOnMouseClickOnProductTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
      pick(event);
    }
  }

  @Override
  public HashSet<Product> get() {
    return result;
  }

  private void getCategoriesFromDatabase() {
    try {
      categoryTable.setItems(categoryRepository.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  private void getProductsFromDatabase(Category category) {
    try {
      productTable.setItems(productRepository.getAll(category));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }
}
