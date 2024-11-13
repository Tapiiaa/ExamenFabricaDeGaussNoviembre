package com.example.examenfabricadegauss.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssemblyStatus {

  private String assemblyId;
  private String status;
  private List<Component> components;

  public AssemblyStatus(String assemblyId) {
    this.assemblyId = assemblyId;
    this.status = "Pending";
    this.components = new ArrayList<>();
  }
  
  public void addComponent(Component component) {
    components.add(component);
  }

  public void removeComponent(Component component) {
    components.remove(component);
  }

  // Verificamos si el ensamblaje está completo y devolvemos un valor booleano en funcion de si el número de componentes es mayor o igual al esperado
  public boolean isComplete(int expectedComponents) {
    return components.size() >= expectedComponents;
  }

  @Override
  public String toString() {
    return "AssemblyStatus [assemblyId=" + assemblyId + ", status=" + status + ", components=" + components + "]";
  }

    public void clearComponents() {
        components.clear();
    }
}
