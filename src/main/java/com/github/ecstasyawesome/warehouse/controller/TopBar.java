package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.module.HomeProvider;
import com.github.ecstasyawesome.warehouse.module.UserListProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;

public class TopBar {

  private final WindowManager windowManager = WindowManager.getInstance();

  @FXML
  private MenuItem homeItem;

  @FXML
  private MenuItem userListItem;

  @FXML
  void logout(ActionEvent event) {
    var result = windowManager
        .showDialog(AlertType.CONFIRMATION, "Are you sure you want to logout?"); // TODO i18n
    if (result.isPresent() && result.get() == ButtonType.OK) {
      SessionManager.erase();
      windowManager.showAuthorization();
    }
  }

  @FXML
  void exit(ActionEvent event) {
    var result = windowManager
        .showDialog(AlertType.CONFIRMATION, "Are you sure you want to exit?"); // TODO i18n
    if (result.isPresent() && result.get() == ButtonType.OK) {
      windowManager.shutdown();
    }
  }

  @FXML
  void showHome(ActionEvent event) {
    windowManager.show(HomeProvider.INSTANCE);
  }

  @FXML
  void showProfile(ActionEvent event) {

  }

  @FXML
  void showSettings(ActionEvent event) {

  }

  @FXML
  void showUserList(ActionEvent event) {
    windowManager.show(UserListProvider.INSTANCE);
  }

  @FXML
  private void initialize() {
    checkModule(HomeProvider.INSTANCE, homeItem);
    checkModule(UserListProvider.INSTANCE, userListItem);
  }

  private void checkModule(ModuleProvider<?> expected, MenuItem menuItem) {
    var actual = windowManager.getCurrentModuleProvider();
    if (actual.getClass().equals(expected.getClass())
        || !windowManager.isAccessGranted(expected.getAccess())) {
      menuItem.setDisable(true);
    }
  }
}
