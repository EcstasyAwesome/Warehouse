package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.AbstractConfiguredFeedbackController;

public abstract class AbstractConfiguredFeedbackModule<T extends
    AbstractConfiguredFeedbackController<E, R>, E, R>
    extends AbstractConfiguredModule<T, E> {

}
