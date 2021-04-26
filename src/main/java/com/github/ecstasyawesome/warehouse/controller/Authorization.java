package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.Controller;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.dao.UserDaoService;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.module.AdministratorRegistrationProvider;
import com.github.ecstasyawesome.warehouse.module.HomeProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;

public class Authorization extends Controller {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final UserDaoService userDaoService = UserDaoService.INSTANCE;

  @FXML
  private TextField loginField;

  @FXML
  private PasswordField passwordField;

  @FXML
  void login() {
    var colorEffect = new ColorAdjust(0, 0.1, 0, 0);
    var user = (User) null;
    try {
      user = userDaoService.get(loginField.getText());
    } catch (NullPointerException exception) {
      loginField.setEffect(colorEffect);
    } catch (Exception exception) {
      windowManager.showDialog(exception);
    }
    if (user != null) {
      loginField.setEffect(null);
      if (passwordField.getText().equals(user.getPassword())) {
        passwordField.setEffect(null);
        SessionManager.store("currentUser", user);
        windowManager.show(HomeProvider.INSTANCE);
      } else {
        passwordField.setEffect(colorEffect);
      }
    }
  }

  @FXML
  private void initialize() {
    if (userDaoService.isEmptyTable()) {
      var result = windowManager.show(AdministratorRegistrationProvider.INSTANCE);
      if (result.isPresent()) {
        loginField.setText(result.get());
      } else {
        Platform.exit();
      }
    }
  }
}

