package com.github.ecstasyawesome.warehouse.controller.provider;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.model.AbstractRecord;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.provider.EditProductProviderModule;
import com.github.ecstasyawesome.warehouse.module.provider.NewProductProviderModule;
import com.github.ecstasyawesome.warehouse.module.provider.ShowProductProviderModule;
import com.github.ecstasyawesome.warehouse.repository.Deletable;
import com.github.ecstasyawesome.warehouse.repository.ProductProviderRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductProviderRepositoryService;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductProviderListController extends AbstractController {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProductProviderRepository productProviderRepository =
      ProductProviderRepositoryService.getInstance();
  private final ShowProductProviderModule showProductProviderModule =
      ShowProductProviderModule.getInstance();
  private final NewProductProviderModule newProductProviderModule =
      NewProductProviderModule.getInstance();
  private final EditProductProviderModule editProductProviderModule =
      EditProductProviderModule.getInstance();
  private final Logger logger = LogManager.getLogger(ProductProviderListController.class);
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private Button addButton;

  @FXML
  private Button showButton;

  @FXML
  private Button editButton;

  @FXML
  private Button deleteButton;

  @FXML
  private TableView<ProductProvider> providerTable;

  @FXML
  private TableColumn<ProductProvider, Long> idColumn;

  @FXML
  private TableColumn<ProductProvider, String> nameColumn;

  @FXML
  private void initialize() {
    addButton.setDisable(!isAccessGranted(sessionUser, newProductProviderModule.getAccess()));

    idColumn.setCellValueFactory(entry -> entry.getValue().idProperty().asObject());
    nameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());

    providerTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevProvider, currentProvider) -> {
          var currentNull = currentProvider == null;
          showButton.setDisable(
              currentNull || !isAccessGranted(sessionUser, showProductProviderModule.getAccess()));
          editButton.setDisable(
              currentNull || !isAccessGranted(sessionUser, editProductProviderModule.getAccess()));
          deleteButton.setDisable(currentNull || !isAccessGranted(sessionUser, Access.ADMIN));
        });

    getProvidersFromDatabase();
  }

  @FXML
  private void add() {
    var result = windowManager.showAndGet(newProductProviderModule);
    result.ifPresent(providerTable.getItems()::add);
  }

  @FXML
  private void delete() {
    deleteRecord("company", providerTable, productProviderRepository);
  }

  @FXML
  private void show() {
    var model = providerTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(showProductProviderModule, model.getSelectedItem());
    }
  }

  @FXML
  private void edit() {
    var model = providerTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(editProductProviderModule, model.getSelectedItem());
    }
  }

  @FXML
  private void doOnKeyReleasedOnTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !showButton.isDisable()) {
      show();
    }
  }

  @FXML
  private void doOnMouseClickTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !showButton.isDisable()) {
      show();
    }
  }

  @FXML
  private void doOnSortTable() {
    providerTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void refresh() {
    getProvidersFromDatabase();
  }

  private <T extends AbstractRecord> void deleteRecord(String name, TableView<T> table,
      Deletable<T> deletable) {
    var selectionModel = table.getSelectionModel();
    if (!selectionModel.isEmpty()) {
      var confirmation = windowManager.showDialog(AlertType.CONFIRMATION,
          "All related data will be removed too, are you sure?"); // TODO i18n
      if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
        var record = selectionModel.getSelectedItem();
        try {
          deletable.delete(record);
          table.getItems().remove(record);
          logger.info("Deleted a {} with id={}", name, record.getId());
        } catch (NullPointerException exception) {
          windowManager.showDialog(exception);
        }
      }
    }
  }

  private void getProvidersFromDatabase() {
    try {
      providerTable.setItems(productProviderRepository.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }
}
