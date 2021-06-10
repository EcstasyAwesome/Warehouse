package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.window.MultiSceneWindow;
import com.github.ecstasyawesome.warehouse.core.window.WindowNode;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthorizationWindow extends MultiSceneWindow {

  private final Logger logger = LogManager.getLogger(AuthorizationWindow.class);

  public AuthorizationWindow(Stage owner) {
    super(owner);
    stage.setResizable(false);
    stage.setOnHidden(getOnCloseAction());
    logger.debug("Initialized");
  }

  @Override
  public void show(String title, Scene scene, WindowNode... windowNodes) {
    closeWindowNodes(windowNodes);
    configureStage(title, scene);
    applyFadeAnimation(scene.getRoot());
    logger.debug("Showed a scene titled with '{}'", stage.getTitle());
    stage.showAndWait();
  }

  private EventHandler<WindowEvent> getOnCloseAction() {
    return event -> {
      var user = (User) SessionManager.get("currentUser").orElse(null);
      if (user != null) {
        logger.debug("Closed with authorized user '{}'", user.getPersonSecurity().getLogin());
      } else {
        logger.debug("Closed without authorized user");
      }
    };
  }

  private void applyFadeAnimation(Parent parent) {
    var fadeTransition = new FadeTransition(Duration.seconds(2), parent);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.setCycleCount(1);
    fadeTransition.playFromStart();
  }
}
