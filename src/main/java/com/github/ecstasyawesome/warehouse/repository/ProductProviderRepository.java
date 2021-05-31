package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.ProductProvider;

public abstract class ProductProviderRepository extends AbstractRepository<ProductProvider>
    implements Producible<ProductProvider>, Readable<ProductProvider>, Updatable<ProductProvider>,
    Deletable<ProductProvider>, Observable<ProductProvider>, UniqueField {

}
