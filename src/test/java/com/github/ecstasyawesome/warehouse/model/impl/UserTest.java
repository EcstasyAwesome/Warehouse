package com.github.ecstasyawesome.warehouse.model.impl;

import static com.github.ecstasyawesome.warehouse.DefaultRecordRepository.createUser;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ecstasyawesome.warehouse.model.Access;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  public void testCopyConstructor() {
    var user1 = createUser("login");
    var user2 = new User(user1);

    assertNotSame(user1.getPersonContact(), user2.getPersonContact());
    assertNotSame(user1.getPersonSecurity(), user2.getPersonSecurity());

    assertNotSame(user1, user2);
    assertEquals(user1, user2);
  }

  @Test
  public void testRecover() {
    var user1 = createUser("login");
    var user2 = User.Builder.create()
        .setId(157)
        .setName("n")
        .setSurname("s")
        .setSecondName("s")
        .setUserSecurity(createSecurity())
        .setUserContact(createContact())
        .build();

    assertNotEquals(user1, user2);
    user2.recover(user1);
    assertEquals(user1, user2);

    assertNotSame(user1.getPersonSecurity(), user2.getPersonSecurity());
    assertNotSame(user1.getPersonContact(), user2.getPersonContact());
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(User.class)
        .withRedefinedSuperclass()
        .usingGetClass()
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void testDefaultBuilderIdValue() {
    var user = User.Builder.create()
        .setName("n")
        .setSurname("s")
        .setSecondName("s")
        .setUserSecurity(createSecurity())
        .setUserContact(createContact())
        .build();

    assertEquals(-1, user.getId());
  }

  @Test
  public void testBuilderRequiredFields() {
    var user = User.Builder.create();
    assertThrows(NullPointerException.class, user::build);
    user.setName("n")
        .setSurname("s")
        .setSecondName("s")
        .setUserSecurity(createSecurity())
        .setUserContact(createContact());
    assertDoesNotThrow(user::build);

    user.setSurname(null);
    assertThrows(NullPointerException.class, user::build);
    user.setSurname("s");

    user.setName(null);
    assertThrows(NullPointerException.class, user::build);
    user.setName("n");

    user.setSecondName(null);
    assertThrows(NullPointerException.class, user::build);
    user.setSecondName("s");

    user.setUserContact(null);
    assertThrows(NullPointerException.class, user::build);
    user.setUserContact(createContact());

    user.setUserSecurity(null);
    assertThrows(NullPointerException.class, user::build);
  }

  private PersonContact createContact() {
    return PersonContact.Builder.create()
        .setEmail("dasdasdasd@mail.com")
        .setPhone("0665269814")
        .build();
  }

  private PersonSecurity createSecurity() {
    return PersonSecurity.Builder.create()
        .setLogin("login_53256")
        .setPassword("qwertyui1234567")
        .setAccess(Access.USER)
        .build();
  }
}