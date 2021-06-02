package com.github.ecstasyawesome.warehouse.controller.impl.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.LOGIN;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.getFieldText;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.PersonContact.Builder;
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

public class NewUserController extends AbstractFeedbackController<User> {

  private final UserRepository userRepository = UserRepositoryService.getInstance();
  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(NewUserController.class);
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
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(passwordField, PASSWORD, false)
        & isFieldValid(loginField, null, LOGIN, userRepository) & isFieldValid(accessChoiceBox)
        && arePasswordsEqual(passwordField, repeatedPasswordField)) {
      var contact = Builder.create()
          .setPhone(getFieldText(phoneField))
          .setEmail(getFieldText(emailField))
          .build();
      var security = PersonSecurity.Builder.create()
          .setLogin(getFieldText(loginField))
          .setPassword(getFieldText(passwordField))
          .setAccess(accessChoiceBox.getValue())
          .build();
      var user = User.Builder.create()
          .setSurname(getFieldText(surnameField))
          .setName(getFieldText(nameField))
          .setSecondName(getFieldText(secondNameField))
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
