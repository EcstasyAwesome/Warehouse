package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;

public abstract class ProductStorageRepository extends AbstractRepository<ProductStorage>
    implements Producible<ProductStorage>, Readable<ProductStorage>, Updatable<ProductStorage>,
    Deletable<ProductStorage>, Observable<ProductStorage>, UniqueField {

}
