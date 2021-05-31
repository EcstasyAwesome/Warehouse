package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.Order;
import com.github.ecstasyawesome.warehouse.model.OrderItem;
import java.util.List;

public abstract class OrderRepository extends AbstractRepository<Order> implements
    Observable<Order> {

  public abstract void create(Order instance, List<OrderItem> items);
}
