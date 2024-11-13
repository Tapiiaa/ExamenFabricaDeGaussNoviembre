package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.model.Component;

import reactor.core.publisher.Mono;


public interface WorkStationService {

  Mono<Component> produceComponent(); // Produce un componente de la maquina.
}
