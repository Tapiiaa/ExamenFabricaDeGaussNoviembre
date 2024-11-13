package com.example.examenfabricadegauss.model;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Component {

  private String id;
  private String type;
  private LocalDateTime timestamp;
  
  public Component(String id, String type) {
    this.id = id;
    this.type = type;
    this.timestamp = LocalDateTime.now();
  }
}
