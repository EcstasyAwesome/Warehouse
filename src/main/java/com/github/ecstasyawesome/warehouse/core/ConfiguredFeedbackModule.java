package com.github.ecstasyawesome.warehouse.core;

import java.net.URL;

public abstract class ConfiguredFeedbackModule<T extends ConfiguredFeedbackController<E>, E>
    extends FeedbackModule<T, E> {

  public ConfiguredFeedbackModule(final URL url) {
    super(url);
  }
}
