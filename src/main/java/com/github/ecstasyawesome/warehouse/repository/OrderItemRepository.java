package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.Order;
import com.github.ecstasyawesome.warehouse.model.OrderItem;

public abstract class OrderItemRepository extends AbstractRepository<OrderItem> implements
    Collectable<OrderItem, Order> {

}
