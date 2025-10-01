package ru.hogwarts.school.controller;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class InfoController {
    private final Environment environment;
    @Autowired
    public InfoController(Environment environment) {
        this.environment = environment;
    }
    @GetMapping("/port")
    public String getPort() {
        String port = environment.getProperty("server.port");
        return port != null ? port : "Порт не установлен";
    }
}
