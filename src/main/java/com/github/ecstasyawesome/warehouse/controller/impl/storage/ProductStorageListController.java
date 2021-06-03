package com.github.ecstasyawesome.warehouse.controller.impl.storage;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.AbstractRecord;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.provider.impl.storage.EditCompanyProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.storage.EditProductStorageProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.storage.NewCompanyProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.storage.NewProductStorageProvider;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.repository.Deletable;
import com.github.ecstasyawesome.warehouse.repository.ProductStorageRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CompanyRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductStorageRepositoryService;
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

public class ProductStorageListController extends AbstractController {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final CompanyRepository companyRepository = CompanyRepositoryService.getInstance();
  private final ProductStorageRepository productStorageRepository =
      ProductStorageRepositoryService.getInstance();
  private final NewCompanyProvider newCompanyProvider = NewCompanyProvider.getInstance();
  private final NewProductStorageProvider newStorageProvider =
      NewProductStorageProvider.getInstance();
  private final EditCompanyProvider editCompanyProvider = EditCompanyProvider.getInstance();
  private final EditProductStorageProvider editStorageProvider =
      EditProductStorageProvider.getInstance();
  private final Logger logger = LogManager.getLogger(ProductStorageListController.class);
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private Button addCompanyButton;

  @FXML
  private Button editCompanyButton;

  @FXML
  private Button deleteCompanyButton;

  @FXML
  private Button addStorageButton;

  @FXML
  private Button editStorageButton;

  @FXML
  private Button deleteStorageButton;

  @FXML
  private TableView<Company> companyTable;

  @FXML
  private TableColumn<Company, String> companyNameColumn;

  @FXML
  private TableView<ProductStorage> storageTable;

  @FXML
  private TableColumn<ProductStorage, Long> storageIdColumn;

  @FXML
  private TableColumn<ProductStorage, String> storageNameColumn;

  @FXML
  private void initialize() {
    addCompanyButton.setDisable(!isAccessGranted(sessionUser, newCompanyProvider.getAccess()));
    addStorageButton.setDisable(!isAccessGranted(sessionUser, newStorageProvider.getAccess()));

    companyNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());
    storageIdColumn.setCellValueFactory(entry -> entry.getValue().idProperty().asObject());
    storageNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());

    companyTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevCategory, currentCategory) -> {
          if (currentCategory != null) {
            getStoragesFromDatabase(currentCategory);
            editCompanyButton
                .setDisable(!isAccessGranted(sessionUser, editCompanyProvider.getAccess()));
            deleteCompanyButton.setDisable(!isAccessGranted(sessionUser, Access.ADMIN));
          } else {
            editCompanyButton.setDisable(true);
            deleteCompanyButton.setDisable(true);
          }
        });

    storageTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevProduct, currentProduct) -> {
          if (currentProduct != null) {
            editStorageButton
                .setDisable(!isAccessGranted(sessionUser, editStorageProvider.getAccess()));
            deleteStorageButton.setDisable(!isAccessGranted(sessionUser, Access.ADMIN));
          } else {
            editStorageButton.setDisable(true);
            deleteStorageButton.setDisable(true);
          }
        });

    getCompaniesFromDatabase();
  }

  @FXML
  private void addCompany() {
    var result = windowManager.showAndGet(newCompanyProvider);
    result.ifPresent(companyTable.getItems()::add);
  }

  @FXML
  private void addStorage() {
    var result = windowManager.showAndGet(newStorageProvider);
    result.ifPresent(storage -> {
      var selectionModel = companyTable.getSelectionModel();
      var company = storage.getCompany();
      if (selectionModel.isEmpty() || company.equals(selectionModel.getSelectedItem())) {
        storageTable.getItems().add(storage);
      }
    });
  }

  @FXML
  private void deleteCompany() {
    deleteRecord("company", companyTable, companyRepository);
  }

  @FXML
  private void deleteStorage() {
    deleteRecord("product storage", storageTable, productStorageRepository);
  }

  @FXML
  private void editCompany() {
    var model = companyTable.getSelectionModel();
    if (!model.isEmpty()) {
      var company = model.getSelectedItem();
      var companyCopy = new Company(company);
      windowManager.showAndWait(editCompanyProvider, company);
      if (!company.equals(companyCopy)) {
        for (var storage : storageTable.getItems()) {
          storage.setCompany(company);
        }
      }
    }
  }

  @FXML
  private void editStorage() {
    var model = storageTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(editStorageProvider, model.getSelectedItem());
    }
  }

  @FXML
  private void onKeyReleasedOnCompanyTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !editCompanyButton.isDisable()) {
      editCompany();
    }
  }

  @FXML
  private void onKeyReleasedOnStorageTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !editStorageButton.isDisable()) {
      editStorage();
    }
  }

  @FXML
  private void onMouseClickOnCompanyTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !editCompanyButton.isDisable()) {
      editCompany();
    }
  }

  @FXML
  private void onMouseClickOnStorageTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !editStorageButton.isDisable()) {
      editStorage();
    }
  }

  @FXML
  private void onSortCompanyTable() {
    onSortAction(companyTable);
  }

  @FXML
  private void onSortStorageTable() {
    onSortAction(storageTable);
  }

  @FXML
  private void refresh() {
    getCompaniesFromDatabase();
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

  private void onSortAction(TableView<?> tableView) {
    var selected = tableView.getSelectionModel();
    if (!selected.isEmpty()) {
      selected.clearSelection();
    }
  }

  private void getCompaniesFromDatabase() {
    var selectionModel = companyTable.getSelectionModel();
    var selectedCompany = (Company) null;
    if (!selectionModel.isEmpty()) {
      selectedCompany = selectionModel.getSelectedItem();
    }
    try {
      companyTable.setItems(companyRepository.getAll());
      if (selectedCompany != null) {
        selectionModel.select(selectedCompany);
      } else {
        companyTable.getSelectionModel().selectFirst();
      }
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  private void getStoragesFromDatabase(Company company) {
    try {
      storageTable.setItems(productStorageRepository.getAll(company));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }
}
