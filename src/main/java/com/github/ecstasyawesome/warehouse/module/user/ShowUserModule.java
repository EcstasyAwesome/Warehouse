package com.github.ecstasyawesome.warehouse.module.user;

import com.github.ecstasyawesome.warehouse.controller.user.ShowUserController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import java.net.URL;

public class ShowUserModule extends AbstractConfiguredModule<ShowUserController, User> {

  private static final ShowUserModule INSTANCE = new ShowUserModule();
  private final URL fxml = getClass().getResource("/model/user/ShowUser.fxml");

  private ShowUserModule() {
  }

  public static ShowUserModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<ShowUserController> create() {
    return new FxmlBundle<>(fxml);
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
