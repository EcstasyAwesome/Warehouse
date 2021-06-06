package com.github.ecstasyawesome.warehouse.controller.order;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.core.controller.Cacheable;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.OrderItem;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.module.product.ProductPickerModule;
import com.github.ecstasyawesome.warehouse.repository.ProductProviderRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductStorageRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductProviderRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductStorageRepositoryService;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class NewOrderController extends AbstractController implements Cacheable {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProductPickerModule productPickerModule = ProductPickerModule.getInstance();
  private final ProductProviderRepository productProviderRepository =
      ProductProviderRepositoryService.getInstance();
  private final ProductStorageRepository productStorageRepository =
      ProductStorageRepositoryService.getInstance();

  @FXML
  private Button createButton;

  @FXML
  private DatePicker datePicker;

  @FXML
  private ChoiceBox<ProductProvider> providerChoiceBox;

  @FXML
  private ChoiceBox<ProductStorage> storageChoiceBox;

  @FXML
  private TextArea commentArea;

  @FXML
  private Button addButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Button clearButton;

  @FXML
  private TableView<OrderItem> orderItemTable;

  @FXML
  private TableColumn<OrderItem, Long> idColumn;

  @FXML
  private TableColumn<OrderItem, String> nameColumn;

  @FXML
  private TableColumn<OrderItem, Category> categoryColumn;

  @FXML
  private TableColumn<OrderItem, Unit> unitColumn;

  @FXML
  private TableColumn<OrderItem, Double> amountColumn;

  @FXML
  private void initialize() {
    idColumn.setCellValueFactory(entry -> entry.getValue().getProduct().idProperty().asObject());
    nameColumn.setCellValueFactory(entry -> entry.getValue().getProduct().nameProperty());
    categoryColumn.setCellValueFactory(entry -> entry.getValue().getProduct().categoryProperty());
    unitColumn.setCellValueFactory(entry -> entry.getValue().getProduct().unitProperty());
    amountColumn.setCellValueFactory(entry -> entry.getValue().amountProperty().asObject());
    amountColumn.setCellFactory(cell -> new TableCell<>() {

      @Override
      public void updateItem(Double value, boolean empty) {
        super.updateItem(value, empty);
        setText(Unit.convert(empty ? 0D : value));
      }
    });

    datePicker.setValue(LocalDate.now());
    try {
      storageChoiceBox.setItems(productStorageRepository.getAll());
      providerChoiceBox.setItems(productProviderRepository.getAll());
    } catch (NumberFormatException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void create() {

  }

  @FXML
  private void add() {
    var result = windowManager.showAndGet(productPickerModule);
    result.ifPresent(products -> {
      for (var product : products) {
        var item = orderItemTable.getItems().stream()
            .filter(entry -> entry.getProduct().equals(product))
            .findAny();
        if (item.isEmpty()) {
          var orderItem = OrderItem.Builder.create()
              .setProduct(product)
              .setAmount(0)
              .build();
          orderItemTable.getItems().add(orderItem);
        }
      }
    });
  }

  @FXML
  private void clear() {

  }

  @FXML
  private void delete() {

  }

  @FXML
  private void editAmount() {

  }

  @FXML
  private void onKeyReleasedOnProductTable(KeyEvent event) {

  }

  @FXML
  private void onMouseClickOnProductTable(MouseEvent event) {

  }

  @FXML
  private void onSortProductTable(SortEvent<?> event) {

  }

  @Override
  public boolean isReady() {
    return false;
  }

  @Override
  public void backup() {

  }

  @Override
  public void recover() {

  }
}
