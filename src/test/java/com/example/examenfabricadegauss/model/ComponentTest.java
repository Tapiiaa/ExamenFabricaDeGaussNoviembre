package com.example.examenfabricadegauss.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class ComponentTest {

  @Test
  void testComponentCreation() {
    Component component = new Component("1234", "Advanced");

    assertEquals("1234", component.getId());
    assertEquals("Advanced", component.getType());
    assertNotNull(component.getTimestamp());
  }
}
