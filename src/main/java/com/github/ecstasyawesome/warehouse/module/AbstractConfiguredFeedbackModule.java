package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredFeedbackController;
import java.net.URL;

public abstract class AbstractConfiguredFeedbackModule<T extends
    AbstractConfiguredFeedbackController<E, R>, E, R> extends AbstractConfiguredModule<T, E> {

  public AbstractConfiguredFeedbackModule(final URL url) {
    super(url);
  }
}
