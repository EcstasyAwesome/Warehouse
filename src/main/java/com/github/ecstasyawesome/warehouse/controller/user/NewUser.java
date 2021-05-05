package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.LOGIN;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRING;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isLoginValid;

import com.github.ecstasyawesome.warehouse.core.Access;
import com.github.ecstasyawesome.warehouse.core.FeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class NewUser extends FeedbackController<User> {

  private final UserDaoService userDaoService = UserDaoService.INSTANCE;
  private final WindowManager windowManager = WindowManager.getInstance();
  private User result;

  @FXML
  private TextField surnameField;

  @FXML
  private TextField nameField;

  @FXML
  private TextField secondNameField;

  @FXML
  private TextField phoneField;

  @FXML
  private ChoiceBox<Access> accessChoiceBox;

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
        & isFieldValid(loginField, LOGIN) & isFieldValid(passwordField, PASSWORD)
        & isFieldValid(accessChoiceBox) && arePasswordsEqual(passwordField, repeatedPasswordField)
        && isLoginValid(loginField)) {
      var user = User.builder()
          .surname(surnameField.getText())
          .name(nameField.getText())
          .secondName(secondNameField.getText())
          .phone(phoneField.getText())
          .login(loginField.getText())
          .password(passwordField.getText())
          .access(accessChoiceBox.getValue())
          .build();
      try {
        user.setId(userDaoService.create(user));
        result = user;
        closeCurrentStage(event);
      } catch (Exception exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @FXML
  private void initialize() {
    accessChoiceBox.setItems(FXCollections.observableArrayList(Access.getAccesses()));
  }

  @Override
  public User get() {
    return result;
  }
}
