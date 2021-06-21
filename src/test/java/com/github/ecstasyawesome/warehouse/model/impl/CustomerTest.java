package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCustomer;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class CustomerTest {

  @Test
  public void testCopyConstructor() {
    var customer1 = createCustomer();
    var customer2 = new Customer(customer1);
    assertNotSame(customer1, customer2);
    assertEquals(customer1, customer2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(Customer.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var customer = Customer.Builder.create()
        .setSurname("s")
        .setName("n")
        .setSecondName("s")
        .setPhone("p")
        .build();
    assertEquals(-1, customer.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var customer = Customer.Builder.create();
    assertThrows(NullPointerException.class, customer::build);
    customer.setSurname("s")
        .setName("n")
        .setSecondName("s")
        .setPhone("p")
        .build();
    assertDoesNotThrow(customer::build);

    customer.setSurname(null);
    assertThrows(NullPointerException.class, customer::build);
    customer.setSurname("s");

    customer.setName(null);
    assertThrows(NullPointerException.class, customer::build);
    customer.setName("n");

    customer.setSecondName(null);
    assertThrows(NullPointerException.class, customer::build);
    customer.setSecondName("s");

    customer.setPhone(null);
    assertThrows(NullPointerException.class, customer::build);
  }
}