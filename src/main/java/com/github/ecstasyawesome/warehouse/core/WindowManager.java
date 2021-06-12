package com.github.ecstasyawesome.warehouse.core;

import static com.github.ecstasyawesome.warehouse.model.Access.isAccessGranted;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractCachedConfiguredController;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractCachedConfiguredFeedbackController;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractCachedController;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractCachedFeedbackController;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredFeedbackController;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractController;
import com.github.ecstasyawesome.warehouse.core.controller.AbstractFeedbackController;
import com.github.ecstasyawesome.warehouse.core.module.AbstractCachedConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.module.AbstractCachedConfiguredModule;
import com.github.ecstasyawesome.warehouse.core.module.AbstractCachedFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.module.AbstractCachedModule;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.module.AbstractConfiguredModule;
import com.github.ecstasyawesome.warehouse.core.module.AbstractFeedbackModule;
import com.github.ecstasyawesome.warehouse.core.module.AbstractModule;
import com.github.ecstasyawesome.warehouse.core.window.MultiSceneWindow;
import com.github.ecstasyawesome.warehouse.core.window.WindowContainer;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import com.github.ecstasyawesome.warehouse.module.user.AuthorizationModule;
import com.github.ecstasyawesome.warehouse.util.SessionManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class WindowManager {

  private static final Logger LOGGER = LogManager.getLogger(WindowManager.class);
  private static WindowManager instance;
  private final Stage root;
  private final MultiSceneWindow authorizationWindow;
  private final MultiSceneWindow workWindow;
  private final WindowContainer extraWindowManager;
  private final Cache cache = new ControllerCache();
  private AbstractModule<? extends AbstractController> currentModule;

  private WindowManager(final Stage root) {
    this.root = root;
    authorizationWindow = new AuthorizationWindow(root);
    workWindow = new WorkWindow(root);
    extraWindowManager = new ExtraModalWindowManager(root);
  }

  public static WindowManager getInstance() {
    if (instance == null) {
      var exception = new IllegalStateException("Window Manager is not initialized");
      throw LOGGER.throwing(Level.FATAL, exception);
    }
    return instance;
  }

  public static void initialize(final Stage stage) {
    if (instance != null) {
      var exception = new IllegalStateException("Window Manager is already initialized");
      throw LOGGER.throwing(Level.FATAL, exception);
    }
    instance = new WindowManager(stage);
    LOGGER.debug("Initialized");
  }

  public void showAuthorization() {
    LOGGER.trace("Request to show the module in the authorization window");
    var user = getUserFromContext();
    if (user.isEmpty()) {
      cache.erase();
      currentModule = AuthorizationModule.getInstance();
      authorizationWindow.show(currentModule.getTitle(), currentModule.create().getScene(),
          workWindow, extraWindowManager);
    } else {
      showDialog(AlertType.WARNING, "Do logout before login"); // TODO i18n
    }
  }

  public <T extends AbstractController> void show(final AbstractModule<T> module) {
    LOGGER.trace("Request to show the module in the work window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, module.getAccess())) {
      currentModule = module;
      workWindow.show(module.getTitle(), module.create().getScene(), authorizationWindow);
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractCachedController<C>, C> void show(
      final AbstractCachedModule<T, C> cachedModule) {
    LOGGER.trace("Request to show the cached module in the work window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, cachedModule.getAccess())) {
      currentModule = cachedModule;
      var fxmlBundle = cachedModule.create();
      cache.check(fxmlBundle.getController());
      workWindow.show(cachedModule.getTitle(), fxmlBundle.getScene(), authorizationWindow);
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractConfiguredController<E>, E> void show(
      final AbstractConfiguredModule<T, E> configuredModule, E instance) {
    LOGGER.trace("Request to show the configured module in the work window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, configuredModule.getAccess())) {
      currentModule = configuredModule;
      var fxmlBundle = configuredModule.create();
      fxmlBundle.getController().apply(instance);
      workWindow.show(configuredModule.getTitle(), fxmlBundle.getScene(), authorizationWindow);
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractCachedConfiguredController<E, C>, E, C> void show(
      final AbstractCachedConfiguredModule<T, E, C> cachedConfiguredModule, E instance) {
    LOGGER.trace("Request to show the cached configured module in the work window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, cachedConfiguredModule.getAccess())) {
      currentModule = cachedConfiguredModule;
      var fxmlBundle = cachedConfiguredModule.create();
      var controller = fxmlBundle.getController();
      cache.check(controller);
      controller.apply(instance);
      workWindow.show(cachedConfiguredModule.getTitle(), fxmlBundle.getScene(),
          authorizationWindow);
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractController> void showAndWait(final AbstractModule<T> module) {
    LOGGER.trace("Request to show the module in the extra window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, module.getAccess())) {
      var fxmlBundle = module.create();
      extraWindowManager.showWindow(module.getTitle(), fxmlBundle.getScene());
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractCachedController<C>, C> void showAndWait(
      final AbstractCachedModule<T, C> cachedModule) {
    LOGGER.trace("Request to show the cached module in the extra window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, cachedModule.getAccess())) {
      var fxmlBundle = cachedModule.create();
      cache.check(fxmlBundle.getController());
      extraWindowManager.showWindow(cachedModule.getTitle(), fxmlBundle.getScene());
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractConfiguredController<E>, E> void showAndWait(
      final AbstractConfiguredModule<T, E> configuredModule, E instance) {
    LOGGER.trace("Request to show the configured module in the extra window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, configuredModule.getAccess())) {
      var fxmlBundle = configuredModule.create();
      fxmlBundle.getController().apply(instance);
      extraWindowManager.showWindow(configuredModule.getTitle(), fxmlBundle.getScene());
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractCachedConfiguredController<E, C>, E, C> void showAndWait(
      final AbstractCachedConfiguredModule<T, E, C> cachedConfiguredModule, E instance) {
    LOGGER.trace("Request to show the cached configured module in the extra window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, cachedConfiguredModule.getAccess())) {
      var fxmlBundle = cachedConfiguredModule.create();
      var controller = fxmlBundle.getController();
      cache.check(controller);
      controller.apply(instance);
      extraWindowManager.showWindow(cachedConfiguredModule.getTitle(), fxmlBundle.getScene());
    } else {
      showAccessWarning();
    }
  }

  public <T extends AbstractFeedbackController<E>, E> Optional<E> showAndGet(
      final AbstractFeedbackModule<T, E> feedbackModule) {
    LOGGER.trace("Request to show the feedback module in the extra window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, feedbackModule.getAccess())) {
      var fxmlBundle = feedbackModule.create();
      extraWindowManager.showWindow(feedbackModule.getTitle(), fxmlBundle.getScene());
      return Optional.ofNullable(fxmlBundle.getController().get());
    } else {
      showAccessWarning();
    }
    return Optional.empty();
  }

  public <T extends AbstractCachedFeedbackController<E, C>, E, C> Optional<E> showAndGet(
      final AbstractCachedFeedbackModule<T, E, C> cachedFeedbackModule) {
    LOGGER.trace("Request to show the cached feedback module in the extra window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, cachedFeedbackModule.getAccess())) {
      var fxmlBundle = cachedFeedbackModule.create();
      var controller = fxmlBundle.getController();
      cache.check(controller);
      extraWindowManager.showWindow(cachedFeedbackModule.getTitle(), fxmlBundle.getScene());
      return Optional.ofNullable(controller.get());
    } else {
      showAccessWarning();
    }
    return Optional.empty();
  }

  public <T extends AbstractConfiguredFeedbackController<E, R>, E, R> Optional<R> showAndGet(
      final AbstractConfiguredFeedbackModule<T, E, R> configuredFeedbackModule, final E instance) {
    LOGGER.trace("Request to show the configured feedback module in the extra window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, configuredFeedbackModule.getAccess())) {
      var fxmlBundle = configuredFeedbackModule.create();
      var controller = fxmlBundle.getController();
      controller.apply(instance);
      extraWindowManager.showWindow(configuredFeedbackModule.getTitle(), fxmlBundle.getScene());
      return Optional.ofNullable(controller.get());
    } else {
      showAccessWarning();
    }
    return Optional.empty();
  }

  public <T extends
      AbstractCachedConfiguredFeedbackController<E, R, C>, E, R, C> Optional<R> showAndGet(
      final AbstractCachedConfiguredFeedbackModule<T, E, R, C> cachedConfiguredFeedbackModule,
      final E instance) {
    LOGGER.trace("Request to show the cached configured feedback module in the extra window");
    var user = getUserFromContext().orElse(null);
    if (isAccessGranted(user, cachedConfiguredFeedbackModule.getAccess())) {
      var fxmlBundle = cachedConfiguredFeedbackModule.create();
      var controller = fxmlBundle.getController();
      cache.check(controller);
      controller.apply(instance);
      extraWindowManager.showWindow(cachedConfiguredFeedbackModule.getTitle(),
          fxmlBundle.getScene());
      return Optional.ofNullable(controller.get());
    } else {
      showAccessWarning();
    }
    return Optional.empty();
  }

  @SuppressWarnings("rawtypes")
  public Class<? extends AbstractModule> getCurrentModuleClass() {
    return Objects.requireNonNull(currentModule.getClass(), "Any module was not shown");
  }

  public void shutdown() {
    LOGGER.debug("Closed");
    root.close();
    Platform.exit();
  }

  // TODO push notifications (will be super to find lib to show native notifications)
  public Optional<ButtonType> showDialog(AlertType type, final String message) {
    var alert = createNewDialog(type, message);
    LOGGER.trace("Showed a dialog");
    return alert.showAndWait();
  }

  public void showDialog(final Exception exception) {
    var alert = createNewDialog(AlertType.ERROR, exception.getMessage());
    var stacktrace = getStacktrace(exception);

    var textArea = new TextArea(stacktrace);
    textArea.setEditable(false);
    textArea.setWrapText(true);
    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    var gridPane = new GridPane();
    gridPane.setMaxWidth(Double.MAX_VALUE);
    gridPane.add(textArea, 0, 0);

    alert.getDialogPane().setExpandableContent(gridPane);
    LOGGER.trace("Showed a dialog with an exception");
    alert.showAndWait();
  }

  private Alert createNewDialog(AlertType type, String message) {
    var alert = new Alert(type);
    alert.setTitle("Notification"); // TODO i18n
    alert.setHeaderText(null);
    var defaultMessage = "Message is not provided"; // TODO i18n
    var messageNullOrBlank = message == null || message.isBlank();
    alert.setContentText(messageNullOrBlank ? defaultMessage : message);
    LOGGER.debug("Created an dialog with type '{}' and message '{}'", type.name(), message);
    return alert;
  }

  private String getStacktrace(Exception exception) {
    var result = new StringWriter();
    try (var printStream = new PrintWriter(result)) {
      exception.printStackTrace(printStream);
    }
    return result.toString();
  }

  private Optional<User> getUserFromContext() {
    var user = (User) SessionManager.get("currentUser").orElse(null);
    return Optional.ofNullable(user);
  }

  private void showAccessWarning() {
    showDialog(AlertType.WARNING, "Access denied!"); // TODO i18n
  }
}
