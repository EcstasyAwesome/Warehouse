package com.github.ecstasyawesome.warehouse.module.impl;

import com.github.ecstasyawesome.warehouse.controller.impl.SettingsController;
import com.github.ecstasyawesome.warehouse.core.FxmlBundle;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import java.net.URL;

public class SettingsModule extends AbstractModule<SettingsController> {

  private static final SettingsModule INSTANCE = new SettingsModule();
  private final URL fxml = getClass().getResource("/model/Settings.fxml");

  private SettingsModule() {
  }

  public static SettingsModule getInstance() {
    return INSTANCE;
  }

  @Override
  public FxmlBundle<SettingsController> create() {
    return new FxmlBundle<>(fxml);
  }

  @Override
  public Access getAccess() {
    return Access.USER;
  }

  @Override
  public String getTitle() {
    return "Settings"; // TODO i18n
  }
}
