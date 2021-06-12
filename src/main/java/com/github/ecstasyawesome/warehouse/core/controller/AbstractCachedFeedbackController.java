package com.github.ecstasyawesome.warehouse.core.controller;

public abstract class AbstractCachedFeedbackController<T, C> extends AbstractFeedbackController<T>
    implements Cacheable<C> {

}
