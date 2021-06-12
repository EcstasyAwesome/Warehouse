package com.github.ecstasyawesome.warehouse.controller.order;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.WILDCARD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractCachedController;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.OrderItem;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.order.OrderListModule;
import com.github.ecstasyawesome.warehouse.module.product.ProductPickerModule;
import com.github.ecstasyawesome.warehouse.repository.OrderRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductProviderRepository;
import com.github.ecstasyawesome.warehouse.repository.ProductStorageRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.OrderRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductProviderRepositoryService;
import com.github.ecstasyawesome.warehouse.repository.impl.ProductStorageRepositoryService;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import java.time.LocalDateTime;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewOrderController extends AbstractCachedController<NewOrderController> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final OrderRepository orderRepository = OrderRepositoryService.getInstance();
  private final ProductPickerModule productPickerModule = ProductPickerModule.getInstance();
  private final OrderListModule orderListModule = OrderListModule.getInstance();
  private final ProductProviderRepository productProviderRepository =
      ProductProviderRepositoryService.getInstance();
  private final ProductStorageRepository productStorageRepository =
      ProductStorageRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(NewOrderController.class);
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private ChoiceBox<ProductProvider> providerChoiceBox;

  @FXML
  private ChoiceBox<ProductStorage> storageChoiceBox;

  @FXML
  private TextArea commentArea;

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
    amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(Unit.getConverter()));

    orderItemTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, prevItem, currentItem) ->
            deleteButton.setDisable(currentItem == null));

    orderItemTable.getItems()
        .addListener((ListChangeListener<? super OrderItem>) change ->
            clearButton.setDisable(change.getList().isEmpty())
        );

    try {
      storageChoiceBox.setItems(productStorageRepository.getAll());
      providerChoiceBox.setItems(productProviderRepository.getAll());
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
    }
  }

  @FXML
  private void create() {
    if (isFieldValid(storageChoiceBox) & isFieldValid(providerChoiceBox)
        & isFieldValid(commentArea, WILDCARD, true)) {
      var zeroValue = orderItemTable.getItems().stream()
          .filter(orderItem -> orderItem.getAmount() == 0D)
          .findAny();
      if (zeroValue.isEmpty()) {
        var order = Order.Builder.create()
            .setProductProvider(providerChoiceBox.getValue())
            .setProductStorage(storageChoiceBox.getValue())
            .setComment(getFieldText(commentArea))
            .setTime(LocalDateTime.now())
            .setUser(sessionUser)
            .build();
        try {
          orderRepository.create(order, orderItemTable.getItems());
          logger.info("Created an order with id={}", order.getId());
          var message = String.format("Order №%d created!", order.getId());
          windowManager.showDialog(AlertType.INFORMATION, message);
          windowManager.show(orderListModule);
        } catch (NullPointerException exception) {
          windowManager.showDialog(exception);
        }
      } else {
        windowManager
            .showDialog(AlertType.WARNING,
                "Edit or remove order items with zero amount!"); // TODO i18n
      }
    }
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
    orderItemTable.getItems().clear();
  }

  @FXML
  private void delete() {
    var model = orderItemTable.getSelectionModel();
    if (!model.isEmpty()) {
      orderItemTable.getItems().remove(model.getSelectedItem());
    }
  }

  @FXML
  private void editAmount(CellEditEvent<OrderItem, Double> event) {
    var orderItem = event.getRowValue();
    orderItem.setAmount(orderItem.getProduct().getUnit().validate(event.getNewValue()));
  }

  @Override
  public boolean isReady() {
    return !orderItemTable.getItems().isEmpty() || !commentArea.getText().isEmpty()
           || storageChoiceBox.getValue() != null || providerChoiceBox.getValue() != null;
  }

  @Override
  public NewOrderController backup() {
    return this;
  }

  @Override
  public void recover(NewOrderController instance) {
    orderItemTable.getItems().addAll(instance.orderItemTable.getItems());
    commentArea.setText(instance.commentArea.getText());
    storageChoiceBox.setValue(instance.storageChoiceBox.getValue());
    providerChoiceBox.setValue(instance.providerChoiceBox.getValue());
  }
}
