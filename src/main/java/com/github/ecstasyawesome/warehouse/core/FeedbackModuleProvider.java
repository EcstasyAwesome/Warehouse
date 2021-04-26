package com.github.ecstasyawesome.warehouse.core;

public abstract class FeedbackModuleProvider<T extends FeedbackController<E>, E>
    extends ModuleProvider<T> {

  @Override
  public abstract FeedbackModule<T, E> create();
}
