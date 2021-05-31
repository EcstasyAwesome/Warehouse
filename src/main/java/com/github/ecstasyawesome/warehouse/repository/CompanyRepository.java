package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.impl.Company;

public abstract class CompanyRepository extends AbstractRepository<Company> implements
    Readable<Company>, Updatable<Company>, Producible<Company>, Deletable<Company>, UniqueField,
    Observable<Company> {

}
