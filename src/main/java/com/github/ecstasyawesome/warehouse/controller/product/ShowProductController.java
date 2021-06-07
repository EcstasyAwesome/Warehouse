package com.github.ecstasyawesome.warehouse.controller.product;


import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ShowProductController extends AbstractConfiguredController<Product> {

  @FXML
  private TextField nameField;

  @FXML
  private TextField unitField;

  @FXML
  private TextField categoryField;

  @Override
  public void apply(Product product) {
    setFieldText(nameField, product.getName());
    setFieldText(unitField, product.getUnit().toString());
    setFieldText(categoryField, product.getCategory().toString());
  }
}
