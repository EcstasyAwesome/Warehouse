package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createCompany;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createInvoice;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductProvider;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createProductStorage;
import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.*;

import com.github.ecstasyawesome.warehouse.model.PersonType;
import java.time.LocalDateTime;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class InvoiceTest {

  @Test
  public void testCopyConstructor() {
    var user = createUser("login");
    var company = createCompany("Name", "123456789");
    var storage = createProductStorage("Storage", company);
    var provider = createProductProvider("Provider");

    var invoice1 = createInvoice(storage, provider, user);
    var invoice2 = new Invoice(invoice1);

    assertNotSame(invoice1.getProductStorage(), invoice2.getProductStorage());
    assertNotSame(invoice1.getProductProvider(), invoice2.getProductProvider());
    assertNotSame(invoice1.getUser(), invoice2.getUser());
    assertNotSame(invoice1.getTime(), invoice2.getTime());

    assertNotSame(invoice1, invoice2);
    assertEquals(invoice1, invoice2);
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(Invoice.class)
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

    var invoice = Invoice.Builder.create()
        .setUser(user)
        .setTime(LocalDateTime.now())
        .setProductStorage(storage)
        .setProductProvider(provider)
        .build();
    assertEquals(-1, invoice.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var user = createUser("login");
    var company = createCompany("Name", "123456789");
    var storage = createProductStorage("Storage", company);
    var provider = createProductProvider("Provider");

    var invoice = Invoice.Builder.create();
    assertThrows(NullPointerException.class, invoice::build);

    invoice.setUser(user)
        .setTime(LocalDateTime.now())
        .setProductStorage(storage)
        .setProductProvider(provider)
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

    invoice.setProductProvider(null);
    assertThrows(NullPointerException.class, invoice::build);
  }
}