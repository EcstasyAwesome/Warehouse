package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.window.SingleSceneWindow;
import com.github.ecstasyawesome.warehouse.core.window.StageBasedWindow;
import com.github.ecstasyawesome.warehouse.core.window.WindowContainer;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtraModalWindowManager extends WindowContainer {

  private final Logger logger = LogManager.getLogger(ExtraModalWindowManager.class);
  private final List<SingleSceneWindow> windows = new ArrayList<>();

  {
    logger.debug("Initialized");
  }

  public ExtraModalWindowManager() {
  }

  public ExtraModalWindowManager(Stage owner) {
    super(owner);
  }

  public ExtraModalWindowManager(StageBasedWindow owner) {
    super(owner);
  }

  public void showWindow(String title, Scene scene) {
    var window = createNewWindow(title, scene);
    window.setActionBeforeClosing(windowEvent -> windows.remove(window));
    windows.add(window);
    window.show();
  }

  @Override
  public int getActiveWindows() {
    return windows.size();
  }

  @Override
  public void close() {
    logger.debug("Closed");
    for (var index = windows.size() - 1; index >= 0; index--) {
      windows.get(index).close();
    }
  }

  @Override
  public boolean isActive() {
    return windows.size() > 0;
  }

  private ExtraModalWindow createNewWindow(String title, Scene scene) {
    var extraWindow = (ExtraModalWindow) null;
    if (windows.isEmpty()) {
      if (owner == null) {
        extraWindow = new ExtraModalWindow(title, scene);
      } else {
        extraWindow = new ExtraModalWindow(owner, title, scene);
      }
    } else {
      var lastElementIndex = windows.size() - 1;
      extraWindow = new ExtraModalWindow(windows.get(lastElementIndex), title, scene);
    }
    windows.add(extraWindow);
    logger.debug("Created a new window");
    return extraWindow;
  }
}
