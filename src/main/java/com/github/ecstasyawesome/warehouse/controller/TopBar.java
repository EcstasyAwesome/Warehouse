package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.module.HomeProvider;
import com.github.ecstasyawesome.warehouse.module.user.UserListProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
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
  void logout() {
    var result = windowManager
        .showDialog(AlertType.CONFIRMATION, "Are you sure you want to logout?"); // TODO i18n
    if (result.isPresent() && result.get() == ButtonType.OK) {
      SessionManager.erase();
      windowManager.showAuthorization();
    }
  }

  @FXML
  void exit() {
    var result = windowManager
        .showDialog(AlertType.CONFIRMATION, "Are you sure you want to exit?"); // TODO i18n
    if (result.isPresent() && result.get() == ButtonType.OK) {
      windowManager.shutdown();
    }
  }

  @FXML
  void showHome() {
    windowManager.show(HomeProvider.INSTANCE);
  }

  @FXML
  void showProfile() {
    // TODO
  }

  @FXML
  void showSettings() {
    // TODO
  }

  @FXML
  void showUserList() {
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
