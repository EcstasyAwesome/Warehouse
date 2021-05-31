package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdministratorRegistration extends FeedbackController<PersonSecurity> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserRepository userRepository = UserRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(AdministratorRegistration.class);
  private PersonSecurity result;

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
  private TextField emailField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField repeatedPasswordField;

  @FXML
  private void initialize() {
    loginField.setText("admin");
  }

  @FXML
  private void register(ActionEvent event) {
    if (isFieldValid(surnameField, STRICT_NAME, false) & isFieldValid(nameField, STRICT_NAME, false)
        & isFieldValid(secondNameField, STRICT_NAME, false) & isFieldValid(phoneField, PHONE, false)
        & isFieldValid(emailField, EMAIL, true) & isFieldValid(passwordField, PASSWORD, false)
        && arePasswordsEqual(passwordField, repeatedPasswordField)) {
      var contact = PersonContact.Builder.create()
          .setPhone(phoneField.getText())
          .setEmail(emailField.getText().isEmpty() ? null : emailField.getText())
          .build();
      var security = PersonSecurity.Builder.create()
          .setLogin(loginField.getText())
          .setPassword(passwordField.getText())
          .setAccess(Access.ROOT)
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
        result = user.getPersonSecurity();
        logger.info("Root user registered");
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public PersonSecurity get() {
    return result;
  }
}
