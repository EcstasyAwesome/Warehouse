package com.github.ecstasyawesome.warehouse.controller.user;

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
import javafx.scene.effect.ColorAdjust;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Authorization extends Controller {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserDao userDaoService = UserDaoService.getInstance();
  private final Logger logger = LogManager.getLogger(Authorization.class);

  @FXML
  private TextField loginField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private void initialize() {
    try {
      if (!userDaoService.hasTableRecords()) {
        var result = windowManager.showAndGet(AdministratorRegistrationProvider.INSTANCE);
        if (result.isPresent()) {
          loginField.setText(result.get());
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
    var colorEffect = new ColorAdjust(0, 0.1, 0, 0);
    var login = loginField.getText();
    try {
      var user = userDaoService.get(login);
      loginField.setEffect(null);
      if (passwordField.getText().equals(user.getPassword())) {
        passwordField.setEffect(null);
        SessionManager.store("currentUser", user);
        logger.info("Log in '{}'", login);
        windowManager.show(HomeProvider.INSTANCE);
      } else {
        passwordField.setEffect(colorEffect);
        logger.info("Try to log in with login '{}' and incorrect password", login);
      }
    } catch (NullPointerException exception) {
      loginField.setEffect(colorEffect);
      logger.info("Try to log in with incorrect login '{}'", login);
    }
  }
}

