package com.github.ecstasyawesome.warehouse.core;

import java.net.URL;

public abstract class FeedbackModuleWrapper<T extends FeedbackController<E>, E> extends
    ModuleWrapper<T> {

  public FeedbackModuleWrapper(Access access, URL url) {
    super(access, url);
  }
}
