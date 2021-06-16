package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.impl.Category;

public abstract class CategoryRepository extends AbstractRepository<Category> implements
    Readable<Category>, Updatable<Category>, Producible<Category>, Deletable<Category>,
    Observable<Category> {

}
