package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.module.controller.Authorization;
import com.github.ecstasyawesome.warehouse.service.EventManager;
import com.github.ecstasyawesome.warehouse.util.ResourceLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public final class WindowManager {

  private static WindowManager instance;
  private final Stage root;
  private final Stage authorizationStage = new Stage();
  private final Stage mainStage = new Stage();
  private final List<Stage> stages = new ArrayList<>();
  private final ViewSettings viewSettings = ViewSettings.getInstance();

  private WindowManager(final Stage root) {
    this.root = root;
    configureMainStage();
    configureAuthorizationStage();
  }

  public static WindowManager getInstance() {
    if (instance == null) {
      throw new IllegalStateException("Window Manager is not initialized");
    }
    return instance;
  }

  public static void initialize(final Stage stage) {
    if (instance != null) {
      throw new IllegalStateException("Window Manager is already initialized");
    }
    instance = new WindowManager(stage);
  }

  public void showAuthorizationForm() {
    if (Authorization.getCurrentUser().isEmpty()) {
      mainStage.close();
      authorizationStage.show();
    } else {
      EventManager.showPopUpWindow(AlertType.WARNING, "Do logout before login"); // TODO i18n
    }
  }

  public <T extends Controller> void show(ModuleFactory<T> factory) {
    var wrapper = factory.create();
    if (Authorization.isAccessGranted(wrapper.getAccess())) {
      authorizationStage.close();
      closeSecondaryStagesIfPresent();
      mainStage.setScene(wrapper.getScene());
      mainStage.show();
    } else {
      showAccessWarning();
    }
  }

  public <T extends FeedbackController<E>, E> Optional<E> show(
      FeedbackModuleFactory<T, E> factory) {
    var wrapper = factory.create();
    if (Authorization.isAccessGranted(wrapper.getAccess())) {
      var stage = createNewStage(wrapper.getScene());
      stages.add(stage);
      stage.showAndWait();
    } else {
      showAccessWarning();
    }
    return Optional.ofNullable(wrapper.getController().take());
  }

  private void closeSecondaryStagesIfPresent() {
    if (stages.size() > 0) {
      stages.get(0).close();
    }
  }

  private void showAccessWarning() {
    EventManager.showPopUpWindow(AlertType.WARNING, "Access denied!"); // TODO i18n
  }

  private Stage createNewStage(Scene scene) {
    var stage = new Stage();
    EventHandler<WindowEvent> onCloseEvent = event -> stages.remove(stage);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(stages.isEmpty() ? mainStage : stages.get(stages.size() - 1));
    stage.setScene(scene);
    stage.setOnCloseRequest(onCloseEvent);
    stage.setOnHidden(onCloseEvent); // TODO check workable with OK and CANCEL buttons
    return stage;
  }

  private void configureMainStage() {
    mainStage.initOwner(root);
    mainStage.setMinWidth(viewSettings.getDefaultWidth());
    mainStage.setMinHeight(viewSettings.getDefaultHeight());
    mainStage.setWidth(viewSettings.getWidth());
    mainStage.setHeight(viewSettings.getHeight());
    mainStage.setMaximized(viewSettings.isMaximized());
    mainStage.setOnCloseRequest(getOnCloseActions());
  }

  private void configureAuthorizationStage() {
    var authorization = ResourceLoader.load(Authorization.FXML);
    applyFadeAnimation(authorization);
    authorizationStage.initOwner(root);
    authorizationStage.setScene(new Scene(authorization));
    authorizationStage.setResizable(false);
  }

  private void applyFadeAnimation(Parent parent) {
    var fadeTransition = new FadeTransition(Duration.seconds(3), parent);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.setCycleCount(1);
    fadeTransition.playFromStart();
  }

  private EventHandler<WindowEvent> getOnCloseActions() {
    return event -> {
      viewSettings.setWidth(mainStage.getWidth());
      viewSettings.setHeight(mainStage.getHeight());
      viewSettings.setMaximized(mainStage.isMaximized());
      viewSettings.save();
    };
  }
}
