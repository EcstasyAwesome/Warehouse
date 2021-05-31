package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.Category;
import com.github.ecstasyawesome.warehouse.model.Product;

public abstract class ProductRepository extends AbstractRepository<Product> implements
    Readable<Product>, Producible<Product>, Updatable<Product>, Deletable<Product>, UniqueField,
    Searchable<Product, String>, Collectable<Product, Category>, Observable<Product> {

}
