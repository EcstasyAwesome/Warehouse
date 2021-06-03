package com.github.ecstasyawesome.warehouse.controller.impl;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.HomeProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.SettingsProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.product.ProductListProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.storage.ProductStorageListProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.user.ProfileProvider;
import com.github.ecstasyawesome.warehouse.provider.impl.user.UserListProvider;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;

public class TopBarController {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProfileProvider profileProvider = ProfileProvider.getInstance();
  private final SettingsProvider settingsProvider = SettingsProvider.getInstance();
  private final HomeProvider homeProvider = HomeProvider.getInstance();
  private final UserListProvider userListProvider = UserListProvider.getInstance();
  private final ProductListProvider productListProvider = ProductListProvider.getInstance();
  private final ProductStorageListProvider productStorageListProvider =
      ProductStorageListProvider.getInstance();
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private MenuItem homeItem;

  @FXML
  private MenuItem userListItem;

  @FXML
  private MenuItem productListItem;

  @FXML
  private MenuItem productStorageListItem;

  @FXML
  private void initialize() {
    checkModule(homeProvider, homeItem);
    checkModule(userListProvider, userListItem);
    checkModule(productListProvider, productListItem);
    checkModule(productStorageListProvider, productStorageListItem);
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
    windowManager.showAndWait(settingsProvider);
  }

  @FXML
  private void showUserList() {
    windowManager.show(userListProvider);
  }


  @FXML
  private void showProductList() {
    windowManager.show(productListProvider);
  }

  @FXML
  private void showProductStorageList() {
    windowManager.show(productStorageListProvider);
  }

  private void checkModule(AbstractModuleProvider<?> provider, MenuItem menuItem) {
    var actual = windowManager.getCurrentModuleProviderClass();
    var expected = provider.getClass();
    if (actual.equals(expected) || !isAccessGranted(sessionUser, provider.getAccess())) {
      menuItem.setDisable(true);
    }
  }
}
