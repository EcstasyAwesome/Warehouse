package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCategory;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createOrderItem;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProduct;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class OrderItemTest {

  @Test
  public void testCopyConstructor() {
    var category = createCategory("category");
    var product = createProduct("product", category);

    var item1 = createOrderItem(product);
    var item2 = new OrderItem(item1);

    assertNotSame(item1.getProduct(), item2.getProduct());

    assertNotSame(item1, item2);
    assertEquals(item1, item2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(OrderItem.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var category = createCategory("category");
    var product = createProduct("product", category);

    var item = OrderItem.Builder.create()
        .setProduct(product)
        .setAmount(10)
        .build();
    assertEquals(-1, item.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var category = createCategory("category");
    var product = createProduct("product", category);

    var item = OrderItem.Builder.create();
    assertThrows(NullPointerException.class, item::build);

    item.setProduct(product)
        .setAmount(10)
        .build();
    assertDoesNotThrow(item::build);

    item.setProduct(null);
    assertThrows(NullPointerException.class, item::build);
    item.setProduct(product);

    item.setAmount(-1);
    assertThrows(NullPointerException.class, item::build);
  }
}