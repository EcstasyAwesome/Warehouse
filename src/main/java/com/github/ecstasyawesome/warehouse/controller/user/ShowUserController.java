package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.setFieldText;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ShowUserController extends AbstractConfiguredController<User> {

  @FXML
  private TextField surnameField;

  @FXML
  private TextField nameField;

  @FXML
  private TextField secondNameField;

  @FXML
  private TextField phoneField;

  @FXML
  private TextField emailField;

  @FXML
  private TextField accessField;

  @Override
  public void apply(User instance) {
    setFieldText(surnameField, instance.getSurname());
    setFieldText(nameField, instance.getName());
    setFieldText(secondNameField, instance.getSecondName());
    setFieldText(phoneField, instance.getPersonContact().getPhone());
    setFieldText(emailField, instance.getPersonContact().getEmail());
    setFieldText(accessField, instance.getPersonSecurity().getAccess().name);
  }
}
