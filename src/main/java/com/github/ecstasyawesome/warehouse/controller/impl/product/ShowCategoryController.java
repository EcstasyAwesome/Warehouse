package com.github.ecstasyawesome.warehouse.controller.impl.product;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;
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
