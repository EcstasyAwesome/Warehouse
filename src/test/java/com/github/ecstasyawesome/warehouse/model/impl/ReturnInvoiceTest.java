package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCustomer;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductStorage;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createReturnInvoice;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class ReturnInvoiceTest {

  @Test
  public void testCopyConstructor() {
    var user = createUser("login");
    var company = createCompany("Name", "123456789");
    var storage = createProductStorage("Storage", company);

    var invoice1 = createReturnInvoice(storage, user);
    var invoice2 = new ReturnInvoice(invoice1);

    assertNotSame(invoice1.getProductStorage(), invoice2.getProductStorage());
    assertNotSame(invoice1.getCustomer(), invoice2.getCustomer());
    assertNotSame(invoice1.getUser(), invoice2.getUser());
    assertNotSame(invoice1.getTime(), invoice2.getTime());

    assertNotSame(invoice1, invoice2);
    assertEquals(invoice1, invoice2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(ReturnInvoice.class)
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

    var invoice = ReturnInvoice.Builder.create()
        .setUser(user)
        .setTime(LocalDateTime.now())
        .setProductStorage(storage)
        .setCustomer(createCustomer())
        .build();
    assertEquals(-1, invoice.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var user = createUser("login");
    var company = createCompany("Name", "123456789");
    var storage = createProductStorage("Storage", company);

    var invoice = ReturnInvoice.Builder.create();
    assertThrows(NullPointerException.class, invoice::build);

    invoice.setUser(user)
        .setTime(LocalDateTime.now())
        .setProductStorage(storage)
        .setCustomer(createCustomer())
        .build();
    assertDoesNotThrow(invoice::build);

    invoice.setTime(null);
    assertThrows(NullPointerException.class, invoice::build);
    invoice.setTime(LocalDateTime.now());

    invoice.setUser(null);
    assertThrows(NullPointerException.class, invoice::build);
    invoice.setUser(user);

    invoice.setProductStorage(null);
    assertThrows(NullPointerException.class, invoice::build);
    invoice.setProductStorage(storage);

    invoice.setCustomer(null);
    assertThrows(NullPointerException.class, invoice::build);
  }
}