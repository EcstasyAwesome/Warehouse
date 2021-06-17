package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.Access;
import com.github.ecstasyawesome.warehouse.model.PersonType;
import com.github.ecstasyawesome.warehouse.model.Unit;
import com.github.ecstasyawesome.warehouse.model.impl.Address;
import com.github.ecstasyawesome.warehouse.model.impl.BusinessContact;
import com.github.ecstasyawesome.warehouse.model.impl.Category;
import com.github.ecstasyawesome.warehouse.model.impl.Company;
import com.github.ecstasyawesome.warehouse.model.impl.Order;
import com.github.ecstasyawesome.warehouse.model.impl.OrderItem;
import com.github.ecstasyawesome.warehouse.model.impl.PersonContact;
import com.github.ecstasyawesome.warehouse.model.impl.PersonSecurity;
import com.github.ecstasyawesome.warehouse.model.impl.Product;
import com.github.ecstasyawesome.warehouse.model.impl.ProductProvider;
import com.github.ecstasyawesome.warehouse.model.impl.ProductStorage;
import com.github.ecstasyawesome.warehouse.model.impl.User;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class AbstractTestEntryRepository {

  public static Category createCategory(String name) {
    return Category.Builder.create()
        .setName(name)
        .setDescription("Text")
        .build();
  }

  public static Product createProduct(String name, Category category) {
    return Product.Builder.create()
        .setName(name)
        .setCategory(category)
        .setUnit(Unit.PC)
        .build();
  }

  public static BusinessContact createBusinessContact() {
    return BusinessContact.Builder.create()
        .setEmail("example@mail.com")
        .setSite("example.com")
        .setPhone("0954446589")
        .setExtraPhone("0957894512")
        .build();
  }

  public static Address createAddress() {
    return Address.Builder.create()
        .setTown("Town")
        .setRegion("Region")
        .setStreet("Street")
        .setNumber("7/3")
        .build();
  }

  public static Company createCompany(String name, String identifierCode) {
    return Company.Builder.create()
        .setBusinessContact(createBusinessContact())
        .setAddress(createAddress())
        .setName(name)
        .setIdentifierCode(identifierCode)
        .setPersonType(PersonType.INDIVIDUAL)
        .build();
  }

  public static User createUser() {
    var contact = PersonContact.Builder.create()
        .setPhone("09578567733")
        .setEmail("example@mail.com")
        .build();
    var security = PersonSecurity.Builder.create()
        .setLogin("login")
        .setPassword("password")
        .setAccess(Access.ROOT)
        .build();
    return User.Builder.create()
        .setUserContact(contact)
        .setUserSecurity(security)
        .setSurname("Surname")
        .setName("Name")
        .setSecondName("SecondName")
        .build();
  }

  public static ProductStorage createProductStorage(Company company) {
    return ProductStorage.Builder.create()
        .setAddress(createAddress())
        .setBusinessContact(createBusinessContact())
        .setCompany(company)
        .setName("Name")
        .build();
  }

  public static ProductProvider createProductProvider() {
    return ProductProvider.Builder.create()
        .setName("Name")
        .setAddress(createAddress())
        .setBusinessContact(createBusinessContact())
        .build();
  }

  public static Order createOrder(User user, ProductStorage storage, ProductProvider provider) {
    return Order.Builder.create()
        .setUser(user)
        .setTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
        .setProductStorage(storage)
        .setProductProvider(provider)
        .setComment("None")
        .build();
  }

  public static List<OrderItem> createOrderItems(Product p1, Product p2) {
    var one = OrderItem.Builder.create().setAmount(5).setProduct(p1).build();
    var two = OrderItem.Builder.create().setAmount(7).setProduct(p2).build();
    return List.of(one, two);
  }
}
