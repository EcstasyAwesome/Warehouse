package com.github.ecstasyawesome.warehouse.core.module;

import com.github.ecstasyawesome.warehouse.core.controller.AbstractConfiguredFeedbackController;

public abstract class AbstractConfiguredFeedbackModule<T extends
    AbstractConfiguredFeedbackController<E, R>, E, R> extends AbstractConfiguredModule<T, E> {

}
