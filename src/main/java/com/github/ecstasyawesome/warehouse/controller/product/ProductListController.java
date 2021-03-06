package com.github.ecstasyawesome.warehouse.controller.product;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.product.EditCategoryModule;
import com.github.ecstasyawesome.warehouse.module.product.EditProductModule;
import com.github.ecstasyawesome.warehouse.module.product.NewCategoryModule;
import com.github.ecstasyawesome.warehouse.module.product.NewProductModule;
import com.github.ecstasyawesome.warehouse.module.product.ShowCategoryModule;
import com.github.ecstasyawesome.warehouse.module.product.ShowProductModule;
import com.github.ecstasyawesome.warehouse.repository.CategoryRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CategoryRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductRepositoryService;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductListController extends AbstractController {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CategoryRepository categoryRepository = CategoryRepositoryService.getInstance();
  private final ProductRepository productRepository = ProductRepositoryService.getInstance();
  private final ShowCategoryModule showCategoryModule = ShowCategoryModule.getInstance();
  private final ShowProductModule showProductModule = ShowProductModule.getInstance();
  private final NewProductModule newProductModule = NewProductModule.getInstance();
  private final NewCategoryModule newCategoryModule = NewCategoryModule.getInstance();
  private final EditProductModule editProductModule = EditProductModule.getInstance();
  private final EditCategoryModule editCategoryModule = EditCategoryModule.getInstance();
  private final Logger logger = LogManager.getLogger(ProductListController.class);
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private Button addCategoryButton;

  @FXML
  private Button addProductButton;

  @FXML
  private Button showCategoryButton;

  @FXML
  private Button showProductButton;

  @FXML
  private Button editCategoryButton;

  @FXML
  private Button deleteCategoryButton;

  @FXML
  private Button editProductButton;

  @FXML
  private Button deleteProductButton;

  @FXML
  private TableView<Category> categoryTable;

  @FXML
  private TableColumn<Category, String> categoryNameColumn;

  @FXML
  private TableView<Product> productTable;

  @FXML
  private TableColumn<Product, Long> productIdColumn;

  @FXML
  private TableColumn<Product, String> productNameColumn;

  @FXML
  private TableColumn<Product, Unit> productUnitColumn;

  @FXML
  private TableColumn<Product, Category> productCategoryColumn;

  @FXML
  private TextField searchField;

  @FXML
  private void initialize() {
    configureButton(addCategoryButton, sessionUser, newCategoryModule.getAccess());

    categoryNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());

    productIdColumn.setCellValueFactory(entry -> entry.getValue().idProperty().asObject());
    productNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());
    productUnitColumn.setCellValueFactory(entry -> entry.getValue().unitProperty());
    productCategoryColumn.setCellValueFactory(entry -> entry.getValue().categoryProperty());

    categoryTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevCategory, currentCategory) -> {
          var currentNull = currentCategory == null;
          if (!currentNull) {
            getProductsFromDatabase(currentCategory);
            searchField.clear();
          }
          configureButton(showCategoryButton, sessionUser, currentNull,
              showCategoryModule.getAccess());
          configureButton(editCategoryButton, sessionUser, currentNull,
              editCategoryModule.getAccess());
          configureButton(deleteCategoryButton, sessionUser, currentNull, Access.ADMIN);
        });

    productTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevProduct, currentProduct) -> {
          var currentNull = currentProduct == null;
          configureButton(showProductButton, sessionUser, currentNull,
              showProductModule.getAccess());
          configureButton(editProductButton, sessionUser, currentNull,
              editProductModule.getAccess());
          configureButton(deleteProductButton, sessionUser, currentNull, Access.ADMIN);
        });

    categoryTable.getItems().addListener((ListChangeListener<? super Category>) change -> {
      var empty = change.getList().isEmpty();
      configureButton(addProductButton, sessionUser, empty, newProductModule.getAccess());
    });

    getCategoriesFromDatabase();
    getProductsFromDatabase(null);
  }

  @FXML
  private void addCategory() {
    var result = windowManager.showAndGet(newCategoryModule);
    result.ifPresent(categoryTable.getItems()::add);
  }

  @FXML
  private void editCategory() {
    var model = categoryTable.getSelectionModel();
    if (!model.isEmpty()) {
      var category = model.getSelectedItem();
      var categoryCopy = new Category(category);
      windowManager.showAndWait(editCategoryModule, category);
      if (!category.equals(categoryCopy)) {
        for (var product : productTable.getItems()) {
          product.getCategory().recover(category);
        }
        productTable.refresh();
      }
    }
  }

  @FXML
  private void deleteCategory() {
    deleteSelectedTableRecord(categoryTable, categoryRepository, windowManager, logger);
  }

  @FXML
  private void addProduct() {
    var result = windowManager.showAndGet(newProductModule);
    result.ifPresent(product -> {
      var selectionModel = categoryTable.getSelectionModel();
      var productCategory = product.getCategory();
      if (selectionModel.isEmpty() || productCategory.equals(selectionModel.getSelectedItem())) {
        productTable.getItems().add(product);
      }
    });
  }

  @FXML
  private void editProduct() {
    var model = productTable.getSelectionModel();
    if (!model.isEmpty()) {
      var product = model.getSelectedItem();
      var categoryIdCopy = product.getCategory().getId();
      windowManager.showAndWait(editProductModule, product);
      var categoryModel = categoryTable.getSelectionModel();
      if (!categoryModel.isEmpty() && product.getCategory().getId() != categoryIdCopy) {
        productTable.getItems().remove(product);
      }
    }
  }

  @FXML
  private void deleteProduct() {
    deleteSelectedTableRecord(productTable, productRepository, windowManager, logger);
  }

  @FXML
  private void refresh() {
    if (searchField.getLength() > 0) {
      search();
    } else if (categoryTable.getSelectionModel().isEmpty()) {
      getProductsFromDatabase(null);
    }
    getCategoriesFromDatabase();
  }

  @FXML
  private void clear() {
    if (searchField.getLength() > 0) {
      searchField.clear();
      getProductsFromDatabase(null);
    } else if (!categoryTable.getSelectionModel().isEmpty()) {
      categoryTable.getSelectionModel().clearSelection();
      getProductsFromDatabase(null);
    }
  }

  @FXML
  private void search() {
    categoryTable.getSelectionModel().clearSelection();
    try {
      productTable.setItems(productRepository.search(searchField.getText()));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void doOnSortCategoryTable() {
    categoryTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void doOnKeyReleasedOnCategoryTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !showCategoryButton.isDisable()) {
      showCategory();
    }
  }

  @FXML
  private void doOnMouseClickOnCategoryTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !showCategoryButton.isDisable()) {
      showCategory();
    }
  }

  @FXML
  private void doOnSortProductTable() {
    productTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void doOnKeyReleasedOnProductTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !showProductButton.isDisable()) {
      showProduct();
    }
  }

  @FXML
  private void doOnMouseClickOnProductTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !showProductButton.isDisable()) {
      showProduct();
    }
  }

  @FXML
  private void showCategory() {
    var selectionModel = categoryTable.getSelectionModel();
    if (!selectionModel.isEmpty()) {
      windowManager.showAndWait(showCategoryModule, selectionModel.getSelectedItem());
    }
  }

  @FXML
  private void showProduct() {
    var selectionModel = productTable.getSelectionModel();
    if (!selectionModel.isEmpty()) {
      windowManager.showAndWait(showProductModule, selectionModel.getSelectedItem());
    }
  }

  private void getCategoriesFromDatabase() {
    var selectionModel = categoryTable.getSelectionModel();
    var selectedCategory = (Category) null;
    if (!selectionModel.isEmpty()) {
      selectedCategory = selectionModel.getSelectedItem();
    }
    var categories = categoryTable.getItems();
    categories.clear();
    try {
      categories.addAll(categoryRepository.getAll());
      if (selectedCategory != null) {
        selectionModel.select(selectedCategory);
      }
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  private void getProductsFromDatabase(Category category) {
    try {
      productTable.setItems(
          category == null ? productRepository.getAll() : productRepository.getAll(category));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }
}
