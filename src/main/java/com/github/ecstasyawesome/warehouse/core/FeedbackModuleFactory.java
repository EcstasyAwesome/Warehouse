package com.github.ecstasyawesome.warehouse.core;

public interface FeedbackModuleFactory<T extends FeedbackController<E>, E>
    extends ModuleFactory<T> {

  FeedbackModuleWrapper<T, E> create();
}
