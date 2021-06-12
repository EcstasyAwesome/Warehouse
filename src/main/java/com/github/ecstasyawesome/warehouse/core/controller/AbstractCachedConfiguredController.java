package com.github.ecstasyawesome.warehouse.core.controller;

public abstract class AbstractCachedConfiguredController<T, C> extends
    AbstractConfiguredController<T> implements Cacheable<C> {

}
