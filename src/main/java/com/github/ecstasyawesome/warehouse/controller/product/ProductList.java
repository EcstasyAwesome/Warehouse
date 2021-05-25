package com.github.ecstasyawesome.warehouse.controller.product;

import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.CategoryDao;
import com.github.ecstasyawesome.warehouse.dao.GenericDao;
import com.github.ecstasyawesome.warehouse.dao.ProductDao;
import com.github.ecstasyawesome.warehouse.dao.impl.CategoryDaoService;
import com.github.ecstasyawesome.warehouse.dao.impl.ProductDaoService;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import com.github.ecstasyawesome.warehouse.model.Record;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.module.product.EditCategoryProvider;
import com.github.ecstasyawesome.warehouse.module.product.EditProductProvider;
import com.github.ecstasyawesome.warehouse.module.product.NewCategoryProvider;
import com.github.ecstasyawesome.warehouse.module.product.NewProductProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductList extends Controller {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CategoryDao categoryDao = CategoryDaoService.getInstance();
  private final ProductDao productDao = ProductDaoService.getInstance();
  private final NewProductProvider newProductProvider = NewProductProvider.getInstance();
  private final NewCategoryProvider newCategoryProvider = NewCategoryProvider.getInstance();
  private final EditProductProvider editProductProvider = EditProductProvider.getInstance();
  private final EditCategoryProvider editCategoryProvider = EditCategoryProvider.getInstance();
  private final Logger logger = LogManager.getLogger(ProductList.class);
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

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
    categoryNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());

    productIdColumn.setCellValueFactory(entry -> entry.getValue().idProperty().asObject());
    productNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());
    productUnitColumn.setCellValueFactory(entry -> entry.getValue().unitProperty());
    productCategoryColumn.setCellValueFactory(entry -> entry.getValue().categoryProperty());

    categoryTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevCategory, currentCategory) -> {
          if (currentCategory != null) {
            getProductsFromDatabase(currentCategory);
            var userAccess = sessionUser.getUserSecurity().getAccess();
            var condition = userAccess.level > Access.ADMIN.level;
            editCategoryButton.setDisable(condition);
            deleteCategoryButton.setDisable(condition);
          } else {
            editCategoryButton.setDisable(true);
            deleteCategoryButton.setDisable(true);
          }
          searchField.clear();
        });

    productTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevProduct, currentProduct) -> {
          if (currentProduct != null) {
            var userAccess = sessionUser.getUserSecurity().getAccess();
            editProductButton.setDisable(false);
            deleteProductButton.setDisable(userAccess.level > Access.ADMIN.level);
          } else {
            editProductButton.setDisable(true);
            deleteProductButton.setDisable(true);
          }
        });

    getCategoriesFromDatabase();
    getProductsFromDatabase(null);
  }

  @FXML
  private void addCategory() {
    var result = windowManager.showAndGet(newCategoryProvider);
    result.ifPresent(categoryTable.getItems()::add);
  }

  @FXML
  private void editCategory() {
    var model = categoryTable.getSelectionModel();
    if (!model.isEmpty()) {
      var categoryCopy = new Category(model.getSelectedItem());
      var result = windowManager.showAndGet(editCategoryProvider, model.getSelectedItem());
      result.ifPresent(category -> {
        if (!category.getName().equals(categoryCopy.getName())) {
          for (var product : productTable.getItems()) {
            product.setCategory(category);
          }
        }
      });
    }
  }

  @FXML
  private void deleteCategory() {
    deleteRecord("category", categoryTable, categoryDao);
  }

  @FXML
  private void addProduct() {
    var result = windowManager.showAndGet(newProductProvider);
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
      var categoryCopy = new Category(model.getSelectedItem().getCategory());
      var result = windowManager.showAndGet(editProductProvider, model.getSelectedItem());
      result.ifPresent(product -> {
        var categoryModel = categoryTable.getSelectionModel();
        if (!categoryModel.isEmpty() && !product.getCategory().equals(categoryCopy)) {
          productTable.getItems().remove(product);
        }
      });
    }
  }

  @FXML
  private void deleteProduct() {
    deleteRecord("product", productTable, productDao);
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
      productTable.setItems(productDao.search(searchField.getText()));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void onSortCategoryTable() {
    onSortAction(categoryTable);
  }

  @FXML
  private void onKeyReleasedOnCategoryTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !editCategoryButton.isDisable()) {
      editCategory();
    }
  }

  @FXML
  private void onMouseClickOnCategoryTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !editCategoryButton.isDisable()) {
      editCategory();
    }
  }

  @FXML
  private void onSortProductTable() {
    onSortAction(productTable);
  }

  @FXML
  private void onKeyReleasedOnProductTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !editProductButton.isDisable()) {
      editProduct();
    }
  }

  @FXML
  private void onMouseClickOnProductTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !editProductButton.isDisable()) {
      editProduct();
    }
  }

  private void onSortAction(TableView<?> tableView) {
    var selected = tableView.getSelectionModel();
    if (!selected.isEmpty()) {
      selected.clearSelection();
    }
  }

  private <T extends Record> void deleteRecord(String name, TableView<T> table,
      GenericDao<T> dao) {
    var selectionModel = table.getSelectionModel();
    if (!selectionModel.isEmpty()) {
      var confirmation = windowManager.showDialog(AlertType.CONFIRMATION,
          "All related data will be removed too, are you sure?"); // TODO i18n
      if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
        try {
          var record = selectionModel.getSelectedItem();
          dao.delete(record.getId());
          table.getItems().remove(record);
          logger.info("Deleted a {} with id={}", name, record.getId());
        } catch (NullPointerException exception) {
          windowManager.showDialog(exception);
        }
      }
    } else {
      windowManager.showDialog(AlertType.INFORMATION, "First choose some item"); // TODO i18n
    }

  }

  private void getCategoriesFromDatabase() {
    var selectionModel = categoryTable.getSelectionModel();
    var selectedCategory = (Category) null;
    if (!selectionModel.isEmpty()) {
      selectedCategory = selectionModel.getSelectedItem();
    }
    try {
      categoryTable.setItems(categoryDao.getAll());
      if (selectedCategory != null) {
        selectionModel.select(selectedCategory);
      }
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  private void getProductsFromDatabase(Category category) {
    try {
      productTable.setItems(category == null ? productDao.getAll() : productDao.getAll(category));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }
}
