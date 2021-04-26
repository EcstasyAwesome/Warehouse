package com.github.ecstasyawesome.warehouse.core;

public interface FeedbackModuleProvider<T extends FeedbackController<E>, E> extends
    ModuleProvider<T> {

  @Override
  FeedbackModule<T, E> create();
}
