package com.github.ecstasyawesome.warehouse.model;

import com.github.ecstasyawesome.warehouse.core.Access;

public class User {  // TODO change me (here for test only)

  private String surname;
  private String firstName;
  private String secondName;
  private Access access;

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public Access getAccess() {
    return access;
  }

  public void setAccess(Access access) {
    this.access = access;
  }
}
