package com.github.ecstasyawesome.warehouse.core;

public abstract class ConfiguredFeedbackModuleProvider<T extends ConfiguredFeedbackController<E>, E>
    extends FeedbackModuleProvider<T, E> {

  @Override
  public abstract ConfiguredFeedbackModule<T, E> create();
}
