package com.github.ecstasyawesome.warehouse.module;

import com.github.ecstasyawesome.warehouse.controller.AbstractFeedbackController;

public abstract class AbstractFeedbackModule<T extends AbstractFeedbackController<E>, E>
    extends AbstractModule<T> {

}
