package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createOrder;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductProvider;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductStorage;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class OrderTest {

  @Test
  public void testCopyConstructor() {
    var user = createUser("login");
    var company = createCompany("Name", "123456789");
    var storage = createProductStorage("Storage", company);
    var provider = createProductProvider("Provider");

    var order1 = createOrder(user, storage, provider);
    var order2 = new Order(order1);

    assertNotSame(order1.getProductStorage(), order2.getProductStorage());
    assertNotSame(order1.getProductProvider(), order2.getProductProvider());
    assertNotSame(order1.getUser(), order2.getUser());
    assertNotSame(order1.getTime(), order2.getTime());

    assertNotSame(order1, order2);
    assertEquals(order1, order2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(Order.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var user = createUser("login");
    var company = createCompany("Name", "123456789");
    var storage = createProductStorage("Storage", company);
    var provider = createProductProvider("Provider");

    var order = Order.Builder.create()
        .setUser(user)
        .setTime(LocalDateTime.now())
        .setProductStorage(storage)
        .setProductProvider(provider)
        .setComment("c")
        .build();
    assertEquals(-1, order.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var user = createUser("login");
    var company = createCompany("Name", "123456789");
    var storage = createProductStorage("Storage", company);
    var provider = createProductProvider("Provider");

    var order = Order.Builder.create();
    assertThrows(NullPointerException.class, order::build);

    order.setUser(user)
        .setTime(LocalDateTime.now())
        .setProductStorage(storage)
        .setProductProvider(provider)
        .setComment("c")
        .build();
    assertDoesNotThrow(order::build);

    order.setTime(null);
    assertThrows(NullPointerException.class, order::build);
    order.setTime(LocalDateTime.now());

    order.setUser(null);
    assertThrows(NullPointerException.class, order::build);
    order.setUser(user);

    order.setProductStorage(null);
    assertThrows(NullPointerException.class, order::build);
    order.setProductStorage(storage);

    order.setProductProvider(null);
    assertThrows(NullPointerException.class, order::build);
    order.setProductProvider(provider);

    order.setComment(null);
    assertDoesNotThrow(order::build);
  }
}