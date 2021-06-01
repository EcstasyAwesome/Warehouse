package com.github.ecstasyawesome.warehouse.controller.impl.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NO_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.RED_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.impl.PersonSecurity;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.repository.UserRepository;
import com.github.ecstasyawesome.warehouse.repository.impl.UserRepositoryService;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProfileController extends AbstractController {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserRepository userRepository = UserRepositoryService.getInstance();
  private final Logger logger = LogManager.getLogger(ProfileController.class);
  private final User currentUser = (User) SessionManager.get("currentUser").orElseThrow();

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
  private PasswordField newPasswordField;

  @FXML
  private PasswordField newRepeatedPasswordField;

  @FXML
  private PasswordField currentPasswordField;

  @FXML
  private void initialize() {
    surnameField.setText(currentUser.getSurname());
    nameField.setText(currentUser.getName());
    secondNameField.setText(currentUser.getSecondName());
    phoneField.setText(currentUser.getPersonContact().getPhone());
    emailField.setText(Objects.requireNonNullElse(currentUser.getPersonContact().getEmail(), ""));
  }

  @FXML
  private void save() {
    if (isFieldValid(surnameField, STRICT_NAME, false) & isFieldValid(nameField, STRICT_NAME, false)
        & isFieldValid(secondNameField, STRICT_NAME, false) & isFieldValid(phoneField, PHONE, false)
        & isFieldValid(emailField, EMAIL, true)) {
      var userCopy = new User(currentUser);
      currentUser.setSurname(surnameField.getText());
      currentUser.setName(nameField.getText());
      currentUser.setSecondName(secondNameField.getText());
      currentUser.getPersonContact().setPhone(phoneField.getText());
      currentUser.getPersonContact()
          .setEmail(emailField.getText().isEmpty() ? null : emailField.getText());
      if (!currentUser.equals(userCopy)) {
        try {
          userRepository.update(currentUser);
          logger.info("The user has edited his own profile");
        } catch (NullPointerException exception) {
          currentUser.setSurname(userCopy.getSurname());
          currentUser.setName(userCopy.getName());
          currentUser.setSecondName(userCopy.getSecondName());
          currentUser.setPersonContact(userCopy.getPersonContact());
          windowManager.showDialog(exception);
        }
      }
    }
  }

  @FXML
  private void changePassword() {
    var userSecurity = currentUser.getPersonSecurity();
    if (userSecurity.getPassword().equals(currentPasswordField.getText())) {
      currentPasswordField.setEffect(NO_ADJUST);
      if (isFieldValid(newPasswordField, PASSWORD, false)
          && arePasswordsEqual(newPasswordField, newRepeatedPasswordField)) {
        var securityCopy = new PersonSecurity(userSecurity);
        userSecurity.setPassword(newPasswordField.getText());
        if (!securityCopy.equals(userSecurity)) {
          try {
            userRepository.update(currentUser);
            currentPasswordField.clear();
            newPasswordField.clear();
            newRepeatedPasswordField.clear();
            logger.info("The user has changed his password");
          } catch (NullPointerException exception) {
            currentUser.setPersonSecurity(securityCopy);
            windowManager.showDialog(exception);
          }
        }
      }
    } else {
      currentPasswordField.setEffect(RED_ADJUST);
    }
  }
}
