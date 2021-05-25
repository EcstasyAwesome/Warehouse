package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.module.HomeProvider;
import com.github.ecstasyawesome.warehouse.module.product.ProductListProvider;
import com.github.ecstasyawesome.warehouse.module.user.ProfileProvider;
import com.github.ecstasyawesome.warehouse.module.user.UserListProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;

public class TopBar {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProfileProvider profileProvider = ProfileProvider.getInstance();
  private final HomeProvider homeProvider = HomeProvider.getInstance();
  private final UserListProvider userListProvider = UserListProvider.getInstance();
  private final ProductListProvider productListProvider = ProductListProvider.getInstance();

  @FXML
  private MenuItem homeItem;

  @FXML
  private MenuItem userListItem;

  @FXML
  private MenuItem productListItem;

  @FXML
  private void initialize() {
    checkModule(homeProvider, homeItem);
    checkModule(userListProvider, userListItem);
    checkModule(productListProvider, productListItem);
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
    windowManager.show(homeProvider);
  }

  @FXML
  private void showProfile() {
    windowManager.showAndWait(profileProvider);
  }

  @FXML
  private void showSettings() {
    // TODO
  }

  @FXML
  private void showUserList() {
    windowManager.show(userListProvider);
  }


  @FXML
  private void showProductList() {
    windowManager.show(productListProvider);
  }

  private void checkModule(ModuleProvider<?> expected, MenuItem menuItem) {
    var actual = windowManager.getCurrentModuleProvider();
    if (actual.getClass().equals(expected.getClass())
        || !windowManager.isAccessGranted(expected.getAccess())) {
      menuItem.setDisable(true);
    }
  }
}
