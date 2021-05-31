package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.OrderItem;

public abstract class OrderItemRepository extends AbstractRepository<OrderItem> implements
    Collectable<OrderItem, Order> {

}
