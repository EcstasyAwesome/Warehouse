package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.LOGIN;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.FeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.PersonContact;
import com.github.ecstasyawesome.warehouse.model.impl.PersonSecurity;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.UserRepositoryService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewUser extends FeedbackController<User> {

  private final UserRepository userRepository = UserRepositoryService.getInstance();
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
  private TextField emailField;

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
    if (isFieldValid(surnameField, STRICT_NAME, false) & isFieldValid(nameField, STRICT_NAME, false)
        & isFieldValid(secondNameField, STRICT_NAME, false) & isFieldValid(phoneField, PHONE, false)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(loginField, null, LOGIN,
        userRepository)
        & isFieldValid(passwordField, PASSWORD, false) & isFieldValid(accessChoiceBox)
        && arePasswordsEqual(passwordField, repeatedPasswordField)) {
      var contact = PersonContact.Builder.create()
          .setPhone(phoneField.getText())
          .setEmail(emailField.getText().isEmpty() ? null : emailField.getText())
          .build();
      var security = PersonSecurity.Builder.create()
          .setLogin(loginField.getText())
          .setPassword(passwordField.getText())
          .setAccess(accessChoiceBox.getValue())
          .build();
      var user = User.Builder.create()
          .setSurname(surnameField.getText())
          .setName(nameField.getText())
          .setSecondName(secondNameField.getText())
          .setUserContact(contact)
          .setUserSecurity(security)
          .build();
      try {
        userRepository.create(user);
        result = user;
        logger.info("Added a user with id={}", user.getId());
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
