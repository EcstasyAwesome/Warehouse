package com.github.ecstasyawesome.warehouse.controller;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.core.WindowManager;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.core.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.module.HomeModule;
import com.github.ecstasyawesome.warehouse.module.SettingsModule;
import com.github.ecstasyawesome.warehouse.module.order.OrderListModule;
import com.github.ecstasyawesome.warehouse.module.product.ProductListModule;
import com.github.ecstasyawesome.warehouse.module.provider.ProductProviderListModule;
import com.github.ecstasyawesome.warehouse.module.storage.ProductStorageListModule;
import com.github.ecstasyawesome.warehouse.module.user.ProfileModule;
import com.github.ecstasyawesome.warehouse.module.user.UserListModule;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;

public class TopBarController {

  private final WindowManager windowManager = WindowManager.getInstance();
  private final ProfileModule profileModule = ProfileModule.getInstance();
  private final SettingsModule settingsModule = SettingsModule.getInstance();
  private final HomeModule homeModule = HomeModule.getInstance();
  private final UserListModule userListModule = UserListModule.getInstance();
  private final OrderListModule orderListModule = OrderListModule.getInstance();
  private final ProductListModule productListModule = ProductListModule.getInstance();
  private final ProductStorageListModule productStorageListModule =
      ProductStorageListModule.getInstance();
  private final ProductProviderListModule productProviderListModule =
      ProductProviderListModule.getInstance();
  private final User sessionUser = (User) SessionManager.get("currentUser").orElseThrow();

  @FXML
  private MenuItem homeItem;

  @FXML
  private MenuItem orderListItem;

  @FXML
  private MenuItem userListItem;

  @FXML
  private MenuItem productListItem;

  @FXML
  private MenuItem productStorageListItem;

  @FXML
  private MenuItem productProviderListItem;

  @FXML
  private void initialize() {
    checkModule(homeModule, homeItem);
    checkModule(orderListModule, orderListItem);
    checkModule(userListModule, userListItem);
    checkModule(productListModule, productListItem);
    checkModule(productStorageListModule, productStorageListItem);
    checkModule(productProviderListModule, productProviderListItem);
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
    windowManager.show(homeModule);
  }

  @FXML
  private void showOrderList() {
    windowManager.show(orderListModule);
  }

  @FXML
  private void showProfile() {
    windowManager.showAndWait(profileModule);
  }

  @FXML
  private void showSettings() {
    windowManager.showAndWait(settingsModule);
  }

  @FXML
  private void showUserList() {
    windowManager.show(userListModule);
  }


  @FXML
  private void showProductList() {
    windowManager.show(productListModule);
  }

  @FXML
  private void showProductStorageList() {
    windowManager.show(productStorageListModule);
  }

  @FXML
  private void showProductProviderList() {
    windowManager.show(productProviderListModule);
  }

  private void checkModule(AbstractModule<?> provider, MenuItem menuItem) {
    var actual = windowManager.getCurrentModuleClass();
    var expected = provider.getClass();
    if (actual.equals(expected) || !isAccessGranted(sessionUser, provider.getAccess())) {
      menuItem.setDisable(true);
    }
  }
}
