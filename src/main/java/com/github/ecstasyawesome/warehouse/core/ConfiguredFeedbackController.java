package com.github.ecstasyawesome.warehouse.core;

import java.util.function.Consumer;

public abstract class ConfiguredFeedbackController<T> extends FeedbackController<T>
    implements Consumer<T> {

}
