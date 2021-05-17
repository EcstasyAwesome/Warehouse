package com.github.ecstasyawesome.warehouse.controller.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.CategoryDao;
import com.github.ecstasyawesome.warehouse.dao.GenericDao;
import com.github.ecstasyawesome.warehouse.dao.ProductDao;
import com.github.ecstasyawesome.warehouse.dao.impl.CategoryDaoService;
import com.github.ecstasyawesome.warehouse.dao.impl.ProductDaoService;
import com.github.ecstasyawesome.warehouse.model.BaseRecord;
import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.module.product.NewCategoryProvider;
import com.github.ecstasyawesome.warehouse.module.product.NewProductProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductList extends Controller {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CategoryDao categoryDao = CategoryDaoService.getInstance();
  private final ProductDao productDao = ProductDaoService.getInstance();
  private final Logger logger = LogManager.getLogger(ProductList.class);
  private final ObservableList<Category> categoryList = FXCollections.observableArrayList();
  private final ObservableList<Product> productList = FXCollections.observableArrayList();

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
    categoryTable.setItems(categoryList);
    productTable.setItems(productList);

    categoryNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    productUnitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
    productCategoryColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(categoryList));
    productCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

    categoryTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevCategory, currentCategory) -> {
          if (currentCategory != null) {
            getProductsFromDatabase(currentCategory);
          }
          searchField.deleteText(0, searchField.getLength());
        });

    getCategoriesFromDatabase();
    getProductsFromDatabase(null);
  }

  @FXML
  private void addCategory() {
    var result = windowManager.showAndGet(NewCategoryProvider.INSTANCE);
    result.ifPresent(categoryList::add);
  }

  @FXML
  private void addProduct() {
    var result = windowManager.showAndGet(NewProductProvider.INSTANCE);
    result.ifPresent(product -> {
      var selectionModel = categoryTable.getSelectionModel();
      var productCategory = product.getCategory();
      if (selectionModel.isEmpty() || productCategory.equals(selectionModel.getSelectedItem())) {
        productList.add(product);
      }
    });
  }

  @FXML
  private void deleteCategory() {
    deleteRecord("category", categoryTable, categoryDao);
  }

  @FXML
  private void deleteProduct() {
    deleteRecord("product", productTable, productDao);
  }

  private <T extends BaseRecord> void deleteRecord(String name, TableView<T> table,
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
      searchField.deleteText(0, searchField.getLength());
    } else if (!categoryTable.getSelectionModel().isEmpty()) {
      categoryTable.getSelectionModel().clearSelection();
    }
    getProductsFromDatabase(null);

  }

  @FXML
  private void search() {
    categoryTable.getSelectionModel().clearSelection();
    productList.clear();
    try {
      productList.addAll(productDao.search(searchField.getText()));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void editCategoryName(CellEditEvent<Category, String> event) {
    var category = event.getRowValue();
    var oldValue = event.getOldValue();
    var newValue = event.getNewValue();
    if (!oldValue.equals(newValue)) {
      if (isFieldValid(newValue, STRICT_NAME, categoryDao)) {
        try {
          category.setName(newValue);
          categoryDao.update(category);
          productList.stream()
              .filter(product -> product.getCategory().getId() == category.getId())
              .forEach(product -> product.setCategory(category));
          productTable.refresh();
          logChanges("category", "name", oldValue, newValue, category);
        } catch (NullPointerException exception) {
          category.setName(oldValue);
          refreshAndShowError(categoryTable, exception);
        }
      } else {
        refreshAndShowDialog(categoryTable, "Incorrect name"); // TODO i18n
      }
    }
  }

  @FXML
  private void editProductCategory(CellEditEvent<Product, Category> event) {
    var product = event.getRowValue();
    var oldValue = event.getOldValue();
    var newValue = event.getNewValue();
    if (!oldValue.equals(newValue)) {
      try {
        product.setCategory(newValue);
        productDao.update(product);
        if (!categoryTable.getSelectionModel().isEmpty()) {
          productList.remove(product);
        }
        logChanges("product", "category", oldValue, newValue, product);
      } catch (NullPointerException exception) {
        product.setCategory(oldValue);
        refreshAndShowError(productTable, exception);
      }
    }
  }

  @FXML
  private void editProductName(CellEditEvent<Product, String> event) {
    var product = event.getRowValue();
    var oldValue = event.getOldValue();
    var newValue = event.getNewValue();
    if (!oldValue.equals(newValue)) {
      if (isFieldValid(newValue, NAME, productDao)) {
        try {
          product.setName(newValue);
          productDao.update(product);
          logChanges("product", "name", oldValue, newValue, product);
        } catch (NullPointerException exception) {
          product.setName(oldValue);
          refreshAndShowError(productTable, exception);
        }
      } else {
        refreshAndShowDialog(productTable, "Incorrect name"); // TODO i18n
      }
    }
  }

  private void getCategoriesFromDatabase() {
    var selectionModel = categoryTable.getSelectionModel();
    var selectedCategory = (Category) null;
    if (!selectionModel.isEmpty()) {
      selectedCategory = selectionModel.getSelectedItem();
    }
    categoryList.clear();
    try {
      categoryList.addAll(categoryDao.getAll());
      if (selectedCategory != null) {
        selectionModel.select(selectedCategory);
      }
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  private void getProductsFromDatabase(Category category) {
    productList.clear();
    try {
      productList.addAll(category == null ? productDao.getAll() : productDao.getAll(category));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  private void refreshAndShowError(TableView<?> tableView, Exception exception) {
    tableView.refresh();
    windowManager.showDialog(exception);
  }

  private void refreshAndShowDialog(TableView<?> tableView, String message) {
    tableView.refresh();
    windowManager.showDialog(AlertType.WARNING, message);
  }

  private void logChanges(String entry, String field, Object oldValue, Object newValue,
      BaseRecord record) {
    logger.info("Edited {} {} from '{}' to '{}' with id={}", entry, field, oldValue, newValue,
        record.getId());
  }
}
