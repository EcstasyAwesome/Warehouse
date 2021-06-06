package com.github.ecstasyawesome.warehouse.core.module;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredController;

public abstract class AbstractConfiguredModule<T extends AbstractConfiguredController<E>, E>
    extends AbstractModule<T> {

}
