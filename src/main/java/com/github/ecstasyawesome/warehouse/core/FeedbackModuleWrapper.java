package com.github.ecstasyawesome.warehouse.core;

import java.net.URL;

public abstract class FeedbackModuleWrapper<T extends FeedbackController<E>, E> extends
    ModuleWrapper<T> {

  public FeedbackModuleWrapper(final String title, Access access, final URL url) {
    super(title, access, url);
  }
}
