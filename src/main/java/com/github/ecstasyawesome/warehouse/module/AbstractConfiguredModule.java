package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredController;

public abstract class AbstractConfiguredModule<T extends AbstractConfiguredController<E>, E>
    extends AbstractModule<T> {

}
