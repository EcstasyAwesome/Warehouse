package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.model.Product;
import java.net.URL;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductList {

  public static final URL FXML = Authorization.class.getResource("/model/ProductList.fxml");

  @FXML
  private Button addButton;

  @FXML
  private Button deleteButton;

  @FXML
  private TableView<Product> productTable;

  @FXML
  private TableColumn<Product, Long> idColumn;

  @FXML
  private TableColumn<Product, String> nameColumn;

  @FXML
  private TableColumn<Product, String> categoryColumn;

  @FXML
  private TableColumn<Product, String> unitColumn;

  @FXML
  void addButtonEvent(ActionEvent event) {
    productTable.getItems().add(new Product(1, "item 1", "category 1", "unit 1"));
  }

  @FXML
  void deleteButtonEvent(ActionEvent event) {
    var list = productTable.getItems();
    var option = Optional.ofNullable(productTable.getSelectionModel());
    option.ifPresent(item -> {
      list.remove(item.getSelectedItem());
      item.clearSelection();
    });
  }

  @FXML
  public void initialize() {
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
    productTable.setItems(FXCollections.observableArrayList());
  }
}
