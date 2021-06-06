package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.OrderItem;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;

public abstract class OrderRepository extends AbstractRepository<Order> implements
    Observable<Order> {

  public abstract void create(Order instance, List<OrderItem> items);

  public abstract ObservableList<Order> search(String partOfId);

  public abstract ObservableList<Order> search(LocalDate date);
}
