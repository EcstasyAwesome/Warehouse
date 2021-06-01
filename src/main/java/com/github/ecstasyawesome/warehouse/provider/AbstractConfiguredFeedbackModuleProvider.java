package com.github.ecstasyawesome.warehouse.provider;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredFeedbackController;

public abstract class AbstractConfiguredFeedbackModuleProvider<T extends AbstractConfiguredFeedbackController<E, R>, E, R>
    extends AbstractConfiguredModuleProvider<T, E> {

}
