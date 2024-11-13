package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.model.Component;

import java.util.concurrent.CompletableFuture;


public interface WorkStationService {

  CompletableFuture<Component> produceComponent(); // Produce un componente de la maquina.
}
