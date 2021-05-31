package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Product;

public abstract class ProductRepository extends AbstractRepository<Product> implements
    Readable<Product>, Producible<Product>, Updatable<Product>, Deletable<Product>, UniqueField,
    Searchable<Product, String>, Collectable<Product, Category>, Observable<Product> {

}
