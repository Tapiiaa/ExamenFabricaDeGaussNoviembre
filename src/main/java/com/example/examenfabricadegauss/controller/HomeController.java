package com.example.examenfabricadegauss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index.html";  // Esto redirige a index.html en la carpeta static
    }
}


