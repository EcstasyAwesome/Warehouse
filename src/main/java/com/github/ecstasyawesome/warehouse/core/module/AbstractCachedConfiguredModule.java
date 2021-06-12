package com.github.ecstasyawesome.warehouse.core.module;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractCachedConfiguredController;

public abstract class AbstractCachedConfiguredModule<T extends
    AbstractCachedConfiguredController<E, C>, E, C> extends AbstractConfiguredModule<T, E> {

}
