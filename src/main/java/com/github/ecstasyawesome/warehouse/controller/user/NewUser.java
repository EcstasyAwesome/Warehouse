package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.LOGIN;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.core.FeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDao;
import com.github.ecstasyawesome.warehouse.dao.impl.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewUser extends FeedbackController<User> {

  private final UserDao userDaoService = UserDaoService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(NewUser.class);
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
  private void initialize() {
    accessChoiceBox.setItems(FXCollections.observableArrayList(Access.getAccesses()));
  }

  @FXML
  private void register(ActionEvent event) {
    if (isFieldValid(surnameField, STRICT_NAME) & isFieldValid(nameField, STRICT_NAME)
        & isFieldValid(secondNameField, STRICT_NAME) & isFieldValid(phoneField, PHONE)
        & isFieldValid(loginField, LOGIN, userDaoService) & isFieldValid(passwordField, PASSWORD)
        & isFieldValid(accessChoiceBox)
        && arePasswordsEqual(passwordField, repeatedPasswordField)) {
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
        var id = userDaoService.create(user);
        user.setId(id);
        logger.info("Added a user with id={}", id);
        result = user;
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public User get() {
    return result;
  }
}
