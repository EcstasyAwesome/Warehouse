package com.github.ecstasyawesome.warehouse.controller.product;


import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ShowCategoryController extends AbstractConfiguredController<Category> {

  @FXML
  private TextField nameField;

  @FXML
  private TextArea descriptionArea;

  @Override
  public void apply(Category instance) {
    setFieldText(nameField, instance.getName());
    setFieldText(descriptionArea, instance.getDescription());
  }
}
