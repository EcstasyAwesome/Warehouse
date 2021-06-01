package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.GuiSettings;
import com.github.ecstasyawesome.warehouse.core.Module;
import com.github.ecstasyawesome.warehouse.core.ModuleProvider;
import com.github.ecstasyawesome.warehouse.model.Access;
import java.net.URL;

public class GuiSettingsProvider extends ModuleProvider<GuiSettings> {

  private static final GuiSettingsProvider INSTANCE = new GuiSettingsProvider();
  private final URL fxml = getClass().getResource("/model/GuiSettings.fxml");

  private GuiSettingsProvider() {
  }

  public static GuiSettingsProvider getInstance() {
    return INSTANCE;
  }

  @Override
  public Module<GuiSettings> create() {
    return new Module<>(fxml) {
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
