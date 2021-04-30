package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRING;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.FeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdministratorRegistration extends FeedbackController<String> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserDaoService userDaoService = UserDaoService.INSTANCE;
  private String result;

  @FXML
  private TextField surnameField;

  @FXML
  private TextField nameField;

  @FXML
  private TextField secondNameField;

  @FXML
  private TextField phoneField;

  @FXML
  private TextField loginField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField repeatedPasswordField;

  @FXML
  void register(ActionEvent event) {
    if (isFieldValid(surnameField, STRING) & isFieldValid(nameField, STRING)
        & isFieldValid(secondNameField, STRING) & isFieldValid(phoneField, PHONE)
        & isFieldValid(passwordField, PASSWORD)
        && arePasswordsEqual(passwordField, repeatedPasswordField)) {
      var user = User.builder()
          .surname(surnameField.getText())
          .name(nameField.getText())
          .secondName(secondNameField.getText())
          .phone(phoneField.getText())
          .login(loginField.getText())
          .password(passwordField.getText())
          .access(Access.ROOT)
          .build();
      try {
        userDaoService.create(user);
        result = user.getLogin();
        closeCurrentStage(event);
      } catch (Exception exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public String get() {
    return result;
  }

  @FXML
  private void initialize() {
    loginField.setText("admin");
  }

}

