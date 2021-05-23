package com.github.ecstasyawesome.warehouse.controller.user;

import static com.github.ecstasyawesome.warehouse.util.InputValidator.NO_ADJUST;
import static com.github.ecstasyawesome.warehouse.util.InputValidator.RED_ADJUST;

import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDao;
import com.github.ecstasyawesome.warehouse.dao.impl.UserDaoService;
import com.github.ecstasyawesome.warehouse.module.HomeProvider;
import com.github.ecstasyawesome.warehouse.module.user.AdministratorRegistrationProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Authorization extends Controller {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserDao userDao = UserDaoService.getInstance();
  private final Logger logger = LogManager.getLogger(Authorization.class);

  @FXML
  private TextField loginField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private void initialize() {
    try {
      if (!userDao.hasTableRecords()) {
        var result = windowManager.showAndGet(AdministratorRegistrationProvider.INSTANCE);
        if (result.isPresent()) {
          loginField.setText(result.get().getLogin());
        } else {
          windowManager.shutdown();
        }
      }
    } catch (NullPointerException exception) {
      windowManager.showDialog(exception);
      windowManager.shutdown();
    }
  }

  @FXML
  private void login() {
    var login = loginField.getText();
    try {
      var user = userDao.get(login);
      loginField.setEffect(NO_ADJUST);
      if (passwordField.getText().equals(user.getUserSecurity().getPassword())) {
        passwordField.setEffect(NO_ADJUST);
        SessionManager.store("currentUser", user);
        logger.info("Log in '{}'", login);
        windowManager.show(HomeProvider.INSTANCE);
      } else {
        passwordField.setEffect(RED_ADJUST);
        logger.info("Try to log in with login '{}' and incorrect password", login);
      }
    } catch (NullPointerException exception) {
      loginField.setEffect(RED_ADJUST);
      logger.info("Try to log in with incorrect login '{}'", login);
    }
  }
}

