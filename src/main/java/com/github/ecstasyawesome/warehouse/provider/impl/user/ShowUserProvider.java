package com.github.ecstasyawesome.warehouse.provider.impl.user;

import com.github.ecstasyawesome.warehouse.controller.impl.user.ShowUserController;
import com.github.ecstasyawesome.warehouse.core.FxmlModule;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.provider.AbstractConfiguredModuleProvider;
import java.net.URL;

public class ShowUserProvider extends AbstractConfiguredModuleProvider<ShowUserController, User> {

  private static final ShowUserProvider INSTANCE = new ShowUserProvider();
  private final URL fxml = getClass().getResource("/model/user/ShowUser.fxml");

  private ShowUserProvider() {
  }

  public static ShowUserProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlModule<ShowUserController> create() {
    return new FxmlModule<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Show user"; // TODO i18n
  }
}
