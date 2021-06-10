package com.github.ecstasyawesome.warehouse.core;

import com.github.ecstasyawesome.warehouse.core.window.SingleSceneWindow;
import com.github.ecstasyawesome.warehouse.core.window.StageBasedWindow;
import com.github.ecstasyawesome.warehouse.core.window.WindowContainer;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExtraModalWindowManager extends WindowContainer {

  private final List<SingleSceneWindow> windows = new ArrayList<>();

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
    if (windows.size() > 0) {
      windows.get(0).close();
    }
  }

  @Override
  public boolean isActive() {
    return windows.size() > 0;
  }

  private ExtraModalWindow createNewWindow(String title, Scene scene) {
    if (windows.isEmpty()) {
      if (owner == null) {
        return new ExtraModalWindow(title, scene);
      }
      return new ExtraModalWindow(owner, title, scene);
    } else {
      var lastElementIndex = windows.size() - 1;
      return new ExtraModalWindow(windows.get(lastElementIndex), title, scene);
    }
  }
}
