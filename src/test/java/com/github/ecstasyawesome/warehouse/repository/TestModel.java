package com.github.ecstasyawesome.warehouse.repository;

import com.github.ecstasyawesome.warehouse.model.AbstractNamedRecord;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestModel extends AbstractNamedRecord {

  private final StringProperty surname = new SimpleStringProperty();

  public TestModel() {
  }

  public TestModel(long id, String name, String surname) {
    setId(id);
    setName(name);
    setSurname(surname);
  }

  public String getSurname() {
    return surname.get();
  }

  public void setSurname(String surname) {
    this.surname.set(surname);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }

    var that = (TestModel) obj;
    return Objects.equals(surname.get(), that.surname.get());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), surname.get());
  }
}
