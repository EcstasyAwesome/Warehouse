package com.github.ecstasyawesome.warehouse.controller.storage;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.storage.EditCompanyModule;
import com.github.ecstasyawesome.warehouse.module.storage.EditProductStorageModule;
import com.github.ecstasyawesome.warehouse.module.storage.NewCompanyModule;
import com.github.ecstasyawesome.warehouse.module.storage.NewProductStorageModule;
import com.github.ecstasyawesome.warehouse.module.storage.ShowCompanyModule;
import com.github.ecstasyawesome.warehouse.module.storage.ShowProductStorageModule;
import com.github.ecstasyawesome.warehouse.repository.CompanyRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductStorageRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.CompanyRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductStorageRepositoryService;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
  private final ShowCompanyModule showCompanyModule = ShowCompanyModule.getInstance();
  private final ShowProductStorageModule showProductStorageModule =
      ShowProductStorageModule.getInstance();
  private final NewCompanyModule newCompanyModule = NewCompanyModule.getInstance();
  private final NewProductStorageModule newProductStorageModule =
      NewProductStorageModule.getInstance();
  private final EditCompanyModule editCompanyModule = EditCompanyModule.getInstance();
  private final EditProductStorageModule editProductStorageModule =
      EditProductStorageModule.getInstance();
  private final Logger logger = LogManager.getLogger(ProductStorageListController.class);
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private Button addCompanyButton;

  @FXML
  private Button showCompanyButton;

  @FXML
  private Button editCompanyButton;

  @FXML
  private Button deleteCompanyButton;

  @FXML
  private Button addStorageButton;

  @FXML
  private Button showStorageButton;

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
    addCompanyButton.setDisable(!isAccessGranted(sessionUser, newCompanyModule.getAccess()));
    addStorageButton.setDisable(!isAccessGranted(sessionUser, newProductStorageModule.getAccess()));

    companyNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());
    storageIdColumn.setCellValueFactory(entry -> entry.getValue().idProperty().asObject());
    storageNameColumn.setCellValueFactory(entry -> entry.getValue().nameProperty());

    companyTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevCompany, currentCompany) -> {
          var currentNull = currentCompany == null;
          if (!currentNull) {
            getStoragesFromDatabase(currentCompany);
          }
          showCompanyButton
              .setDisable(currentNull
                          || !isAccessGranted(sessionUser, showCompanyModule.getAccess()));
          editCompanyButton
              .setDisable(currentNull
                          || !isAccessGranted(sessionUser, editCompanyModule.getAccess()));
          deleteCompanyButton.setDisable(currentNull
                                         || !isAccessGranted(sessionUser, Access.ADMIN));
        });

    storageTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevStorage, currentStorage) -> {
          var currentNull = currentStorage == null;
          showStorageButton
              .setDisable(currentNull
                          || !isAccessGranted(sessionUser, showProductStorageModule.getAccess()));
          editStorageButton
              .setDisable(currentNull
                          || !isAccessGranted(sessionUser, editProductStorageModule.getAccess()));
          deleteStorageButton.setDisable(currentNull
                                         || !isAccessGranted(sessionUser, Access.ADMIN));
        });

    getCompaniesFromDatabase();
  }

  @FXML
  private void addCompany() {
    var result = windowManager.showAndGet(newCompanyModule);
    result.ifPresent(companyTable.getItems()::add);
  }

  @FXML
  private void addStorage() {
    var result = windowManager.showAndGet(newProductStorageModule);
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
    deleteSelectedTableRecord(companyTable, companyRepository, windowManager, logger);
  }

  @FXML
  private void deleteStorage() {
    deleteSelectedTableRecord(storageTable, productStorageRepository, windowManager, logger);
  }

  @FXML
  private void editCompany() {
    var model = companyTable.getSelectionModel();
    if (!model.isEmpty()) {
      var company = model.getSelectedItem();
      var companyCopy = new Company(company);
      windowManager.showAndWait(editCompanyModule, company);
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
      windowManager.showAndWait(editProductStorageModule, model.getSelectedItem());
    }
  }

  @FXML
  private void showCompany() {
    var model = companyTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(showCompanyModule, model.getSelectedItem());
    }
  }

  @FXML
  private void showStorage() {
    var model = storageTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(showProductStorageModule, model.getSelectedItem());
    }
  }

  @FXML
  private void doOnKeyReleasedOnCompanyTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !showCompanyButton.isDisable()) {
      showCompany();
    }
  }

  @FXML
  private void doOnKeyReleasedOnStorageTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !showStorageButton.isDisable()) {
      showStorage();
    }
  }

  @FXML
  private void doOnMouseClickOnCompanyTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !showCompanyButton.isDisable()) {
      showCompany();
    }
  }

  @FXML
  private void doOnMouseClickOnStorageTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !showStorageButton.isDisable()) {
      showStorage();
    }
  }

  @FXML
  private void doOnSortCompanyTable() {
    companyTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void doOnSortStorageTable() {
    storageTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void refresh() {
    getCompaniesFromDatabase();
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
