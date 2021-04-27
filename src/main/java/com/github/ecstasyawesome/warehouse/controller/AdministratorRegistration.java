package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.FeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.User;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;

public class AdministratorRegistration extends FeedbackController<String> {

  private final ColorAdjust redAdjust = new ColorAdjust(0, 0.1, 0, 0);
  private final Pattern phonePattern = Pattern.compile("^((\\+?3)?8)?\\d{10}$");
  private final Pattern stringPattern = Pattern.compile("^[A-ZА-Я][A-zА-я]+$");
  private final Pattern passwordPattern = Pattern.compile("^.{8,20}$");
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
    if (isFieldValid(surnameField, stringPattern) & isFieldValid(nameField, stringPattern)
        & isFieldValid(secondNameField, stringPattern) & isFieldValid(phoneField, phonePattern)
        & isFieldValid(passwordField, passwordPattern) & isPasswordsEqual()) {
      var user = User.builder()
          .surname(surnameField.getText())
          .name(nameField.getText())
          .secondName(secondNameField.getText())
          .phone(phoneField.getText())
          .login(loginField.getText())
          .password(passwordField.getText())
          .access(Access.ADMIN)
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

  private boolean isPasswordsEqual() {
    var password = passwordField.getText();
    var repeatedPassword = repeatedPasswordField.getText();
    if (repeatedPassword.isEmpty() || !password.equals(repeatedPassword)) {
      repeatedPasswordField.setEffect(redAdjust);
      return false;
    }
    repeatedPasswordField.setEffect(null);
    return true;
  }

  private boolean isFieldValid(TextField field, Pattern pattern) {
    if (field.getText().matches(pattern.pattern())) {
      field.setEffect(null);
      return true;
    }
    field.setEffect(redAdjust);
    return false;
  }
}

