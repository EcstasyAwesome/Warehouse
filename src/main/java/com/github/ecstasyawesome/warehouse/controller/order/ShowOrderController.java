package com.github.ecstasyawesome.warehouse.controller.order;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.OrderItem;
import com.github.ecstasyawesome.warehouse.module.provider.ShowProductProviderModule;
import com.github.ecstasyawesome.warehouse.module.storage.ShowProductStorageModule;
import com.github.ecstasyawesome.warehouse.module.user.ShowUserModule;
import com.github.ecstasyawesome.warehouse.repository.OrderItemRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.OrderItemRepositoryService;
import com.github.ecstasyawesome.warehouse.util.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ShowOrderController extends AbstractConfiguredController<Order> {

  protected final ShowUserModule showUserModule = ShowUserModule.getInstance();
  private final ShowProductProviderModule showProductProviderModule =
      ShowProductProviderModule.getInstance();
  private final ShowProductStorageModule showProductStorageModule =
      ShowProductStorageModule.getInstance();
  private final OrderItemRepository orderItemRepository = OrderItemRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private Order order;

  @FXML
  private Label providerLabel;

  @FXML
  private Label dateLabel;

  @FXML
  private Label storageLabel;

  @FXML
  private Label userLabel;

  @FXML
  private Label commentLabel;

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
  }

  @FXML
  private void print() { // TODO
    System.out.printf("Printing order with id=%d. Amount of items is %d", order.getId(),
        orderItemTable.getItems().size());
  }

  @FXML
  private void showProvider(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
      windowManager.showAndWait(showProductProviderModule, order.getProductProvider());
    }
  }

  @FXML
  private void showStorage(MouseEvent event) {
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
      windowManager.showAndWait(showProductStorageModule, order.getProductStorage());
    }
  }

  @FXML
  private void showUser(MouseEvent event) {
    var user = order.getUser();
    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && user != null) {
      windowManager.showAndWait(showUserModule, user);
    }
  }

  @Override
  public void apply(Order instance) {
    order = instance;
    providerLabel.setText(instance.getProductProvider().getName());
    storageLabel.setText(instance.getProductStorage().getName());
    dateLabel.setText(instance.getTime().format(Constants.DATE_TIME_FORMATTER));
    commentLabel.setText(instance.getComment());
    var user = instance.getUser();
    if (user != null) {
      userLabel.setText(user.toString());
    }
    try {
      orderItemTable.setItems(orderItemRepository.getAll(instance));
    } catch (NullPointerException exception) {
      exception.printStackTrace();
      windowManager.showDialog(exception);
    }
  }
}
