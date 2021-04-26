package com.github.ecstasyawesome.warehouse.core;

import java.net.URL;

public abstract class FeedbackModule<T extends FeedbackController<E>, E> extends Module<T> {

  public FeedbackModule(final URL url) {
    super(url);
  }
}
