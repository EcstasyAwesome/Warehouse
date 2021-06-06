package com.github.ecstasyawesome.warehouse.controller;

import com.github.ecstasyawesome.warehouse.core.ApplicationSettings;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.util.ResourceLoader.Language;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingsController extends AbstractController {

  private final ApplicationSettings applicationSettings = ApplicationSettings.getInstance();
  private final Logger logger = LogManager.getLogger(SettingsController.class);

  @FXML
  private ChoiceBox<Language> languageChoiceBox;

  @FXML
  private void initialize() {
    languageChoiceBox.setItems(Language.getLanguages());
    languageChoiceBox.setValue(applicationSettings.getLanguage());
  }

  @FXML
  private void save(ActionEvent event) {
    applicationSettings.setLanguage(languageChoiceBox.getValue());
    logger.info("User has changed application settings");
    closeCurrentStage(event);
  }
}
