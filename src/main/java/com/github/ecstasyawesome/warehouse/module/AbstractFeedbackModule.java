package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.AbstractFeedbackController;
import java.net.URL;

public abstract class AbstractFeedbackModule<T extends AbstractFeedbackController<E>, E> extends
    AbstractModule<T> {

  public AbstractFeedbackModule(final URL url) {
    super(url);
  }
}
