package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.PASSWORD;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.PHONE;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.STRING;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.arePasswordsEqual;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.isFieldValid;

import com.github.ecstasyawesome.warehouse.core.ConfiguredFeedbackController;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDao;
import com.github.ecstasyawesome.warehouse.dao.impl.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profile extends ConfiguredFeedbackController<User> {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserDao userDao = UserDaoService.getInstance();
  private final Logger logger = LogManager.getLogger(Profile.class);
  private User currentUser;

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
  private void save(ActionEvent event) {
    if (isFieldValid(surnameField, STRING) & isFieldValid(nameField, STRING)
        & isFieldValid(secondNameField, STRING) & isFieldValid(phoneField, PHONE)
        & isFieldValid(passwordField, PASSWORD)
        && arePasswordsEqual(passwordField, repeatedPasswordField)) {
      currentUser.setSurname(surnameField.getText());
      currentUser.setName(nameField.getText());
      currentUser.setSecondName(secondNameField.getText());
      currentUser.setPhone(phoneField.getText());
      currentUser.setPassword(passwordField.getText());
      try {
        userDao.update(currentUser);
        SessionManager.store("currentUser", currentUser);
        logger.info("The user has edited his own profile");
        closeCurrentStage(event);
      } catch (NullPointerException exception) {
        windowManager.showDialog(exception);
      }
    }
  }

  @Override
  public void accept(final User user) {
    try {
      currentUser = userDao.get(user.getId());
    } catch (NullPointerException exception) {
      logger.fatal("Cannot get session user from database");
      windowManager.showDialog(exception);
      windowManager.shutdown();
    }
    surnameField.setText(currentUser.getSurname());
    nameField.setText(currentUser.getName());
    secondNameField.setText(currentUser.getSecondName());
    phoneField.setText(currentUser.getPhone());
    loginField.setText(currentUser.getLogin());
  }

  @Override
  public User get() {
    return currentUser;
  }
}
