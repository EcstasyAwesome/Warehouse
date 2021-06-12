package com.github.ecstasyawesome.warehouse.core.controller;

public abstract class AbstractCachedConfiguredFeedbackController<T, E, C> extends
    AbstractConfiguredFeedbackController<T, E> implements Cacheable<C> {

}
