package com.github.ecstasyawesome.warehouse.provider.impl;

import com.github.ecstasyawesome.warehouse.controller.impl.SettingsController;
import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.provider.AbstractModuleProvider;
import java.net.URL;

public class SettingsProvider extends AbstractModuleProvider<SettingsController> {

  private static final SettingsProvider INSTANCE = new SettingsProvider();
  private final URL fxml = getClass().getResource("/model/Settings.fxml");

  private SettingsProvider() {
  }

  public static SettingsProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public AbstractModule<SettingsController> create() {
    return new AbstractModule<>(fxml) {
    };
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
