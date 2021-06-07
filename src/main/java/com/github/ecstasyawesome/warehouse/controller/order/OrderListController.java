package com.github.ecstasyawesome.warehouse.controller.order;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.order.NewOrderModule;
import com.github.ecstasyawesome.warehouse.module.order.ShowOrderModule;
import com.github.ecstasyawesome.warehouse.repository.OrderRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.OrderRepositoryService;
import com.github.ecstasyawesome.warehouse.util.Constants;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import java.time.LocalDateTime;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class OrderListController extends AbstractController {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final OrderRepository orderRepository = OrderRepositoryService.getInstance();
  private final NewOrderModule newOrderModule = NewOrderModule.getInstance();
  private final ShowOrderModule showOrderModule = ShowOrderModule.getInstance();
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private Button addButton;

  @FXML
  private Button showButton;

  @FXML
  private TextField searchField;

  @FXML
  private DatePicker datePicker;

  @FXML
  private TableView<Order> orderTable;

  @FXML
  private TableColumn<Order, Long> idColumn;

  @FXML
  private TableColumn<Order, ProductStorage> storageColumn;

  @FXML
  private TableColumn<Order, ProductProvider> providerColumn;

  @FXML
  private TableColumn<Order, LocalDateTime> dateColumn;

  @FXML
  private void initialize() {
    configureButton(addButton, sessionUser, newOrderModule.getAccess());

    idColumn.setCellValueFactory(entry -> entry.getValue().idProperty().asObject());
    storageColumn.setCellValueFactory(entry -> entry.getValue().productStorageProperty());
    providerColumn.setCellValueFactory(entry -> entry.getValue().productProviderProperty());
    dateColumn.setCellValueFactory(entry -> entry.getValue().timeProperty());

    dateColumn.setCellFactory(col -> new TableCell<>() {

      @Override
      protected void updateItem(LocalDateTime item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : item.format(Constants.DATE_TIME_FORMATTER));
      }
    });

    orderTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevOrder, currentOrder) ->
            configureButton(showButton, sessionUser, currentOrder == null,
                showOrderModule.getAccess()));

    getOrdersFromDatabase();
  }

  @FXML
  private void refresh() {
    if (searchField.getLength() > 0) {
      searchById();
    } else if (datePicker.getValue() != null) {
      searchByDate();
    } else {
      getOrdersFromDatabase();
    }
  }

  @FXML
  private void clear() {
    if (searchField.getLength() > 0) {
      searchField.clear();
      getOrdersFromDatabase();
    } else if (datePicker.getValue() != null) {
      datePicker.setValue(null);
      getOrdersFromDatabase();
    }
  }

  @FXML
  private void searchByDate() {
    var localDate = datePicker.getValue();
    if (localDate != null) {
      searchField.clear();
      try {
        orderTable.setItems(orderRepository.search(localDate));
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @FXML
  private void searchById() {
    datePicker.setValue(null);
    try {
      orderTable.setItems(orderRepository.search(searchField.getText()));
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void add() {
    windowManager.show(newOrderModule);
  }

  @FXML
  private void doOnKeyReleasedOnOrderTable(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER && !showButton.isDisable()) {
      show();
    }
  }

  @FXML
  private void doOnMouseClickOnOrderTable(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2
        && !showButton.isDisable()) {
      show();
    }
  }

  @FXML
  private void doOnSortOrderTable() {
    orderTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void show() {
    var model = orderTable.getSelectionModel();
    if (!model.isEmpty()) {
      windowManager.showAndWait(showOrderModule, model.getSelectedItem());
    }
  }

  private void getOrdersFromDatabase() {
    try {
      orderTable.setItems(orderRepository.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }
}

