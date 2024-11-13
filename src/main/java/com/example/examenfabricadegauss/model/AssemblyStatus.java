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

  //GETTERS
  public String getStatus() {
    return status;
  }

  public List<Component> getComponents() {
    return components;
  }

  public String getAssemblyId() {
    return assemblyId;
  }

  //SETTERS

  public void setStatus(String status) {
    this.status = status;
  }

  public void setComponents(List<Component> components) {
    this.components = components;
  }

  public void setAssemblyId(String assemblyId) {
    this.assemblyId = assemblyId;
  }

  // Verificamos si el ensamblaje está completo y devolvemos un valor booleano en funcion de si el número de componentes es mayor o igual al esperado
  public boolean isComplete(int expectedComponents) {
    return components.size() >= expectedComponents;
  }

  @Override
  public String toString() {
    return "AssemblyStatus [assemblyId=" + assemblyId + ", status=" + status + ", components=" + components + "]";
  }
}
