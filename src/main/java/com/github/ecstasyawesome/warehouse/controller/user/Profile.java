package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.EMAIL;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.NO_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.RED_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRICT_NAME;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDao;
import com.github.ecstasyawesome.warehouse.dao.impl.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.model.impl.UserSecurity;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profile extends Controller {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserDao userDao = UserDaoService.getInstance();
  private final Logger logger = LogManager.getLogger(Profile.class);
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
    phoneField.setText(currentUser.getUserContact().getPhone());
    emailField.setText(Objects.requireNonNullElse(currentUser.getUserContact().getEmail(), ""));
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
      currentUser.getUserContact().setPhone(phoneField.getText());
      currentUser.getUserContact()
          .setEmail(emailField.getText().isEmpty() ? null : emailField.getText());
      if (!currentUser.equals(userCopy)) {
        try {
          userDao.update(currentUser);
          logger.info("The user has edited his own profile");
        } catch (NullPointerException exception) {
          currentUser.setSurname(userCopy.getSurname());
          currentUser.setName(userCopy.getName());
          currentUser.setSecondName(userCopy.getSecondName());
          currentUser.setUserContact(userCopy.getUserContact());
          windowManager.showDialog(exception);
        }
      }
    }
  }

  @FXML
  private void changePassword() {
    var userSecurity = currentUser.getUserSecurity();
    if (userSecurity.getPassword().equals(currentPasswordField.getText())) {
      currentPasswordField.setEffect(NO_ADJUST);
      if (isFieldValid(newPasswordField, PASSWORD, false)
          && arePasswordsEqual(newPasswordField, newRepeatedPasswordField)) {
        var securityCopy = new UserSecurity(userSecurity);
        userSecurity.setPassword(newPasswordField.getText());
        if (!securityCopy.equals(userSecurity)) {
          try {
            userDao.update(currentUser);
            currentPasswordField.clear();
            newPasswordField.clear();
            newRepeatedPasswordField.clear();
            logger.info("The user has changed his password");
          } catch (NullPointerException exception) {
            currentUser.setUserSecurity(securityCopy);
            windowManager.showDialog(exception);
          }
        }
      }
    } else {
      currentPasswordField.setEffect(RED_ADJUST);
    }
  }
}
