package com.example.examenfabricadegauss.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Bienvenido a la Aplicación de Examen Fábrica de Gauss";
    }
}

