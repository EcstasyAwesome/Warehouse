package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.User;
import com.github.ecstasyawesome.warehouse.module.HomeProvider;
import com.github.ecstasyawesome.warehouse.module.user.ProfileProvider;
import com.github.ecstasyawesome.warehouse.module.user.UserListProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TopBar {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final Logger logger = LogManager.getLogger(TopBar.class);

  @FXML
  private MenuItem homeItem;

  @FXML
  private MenuItem userListItem;

  @FXML
  private void initialize() {
    checkModule(HomeProvider.INSTANCE, homeItem);
    checkModule(UserListProvider.INSTANCE, userListItem);
  }

  @FXML
  private void logout() {
    var result = windowManager
        .showDialog(AlertType.CONFIRMATION, "Are you sure you want to logout?"); // TODO i18n
    if (result.isPresent() && result.get() == ButtonType.OK) {
      SessionManager.erase();
      windowManager.showAuthorization();
    }
  }

  @FXML
  private void exit() {
    var result = windowManager
        .showDialog(AlertType.CONFIRMATION, "Are you sure you want to exit?"); // TODO i18n
    if (result.isPresent() && result.get() == ButtonType.OK) {
      windowManager.shutdown();
    }
  }

  @FXML
  private void showHome() {
    windowManager.show(HomeProvider.INSTANCE);
  }

  @FXML
  private void showProfile() {
    var currentUser = SessionManager.get("currentUser");
    if (currentUser.isPresent()) {
      windowManager.showAndGet(ProfileProvider.INSTANCE, (User) currentUser.get());
    } else {
      logger.fatal("Unbelievable happens. Session user is absent");
      windowManager.showDialog(AlertType.ERROR, "Session user is absent!"); // TODO i18n
      windowManager.shutdown();
    }

  }

  @FXML
  private void showSettings() {
    // TODO
  }

  @FXML
  private void showUserList() {
    windowManager.show(UserListProvider.INSTANCE);
  }

  private void checkModule(ModuleProvider<?> expected, MenuItem menuItem) {
    var actual = windowManager.getCurrentModuleProvider();
    if (actual.getClass().equals(expected.getClass())
        || !windowManager.isAccessGranted(expected.getAccess())) {
      menuItem.setDisable(true);
    }
  }
}
