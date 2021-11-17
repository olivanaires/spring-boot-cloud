package br.com.ota.udemy.accountmanagerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountManagerController {

    @Autowired
    private Environment env;

    @GetMapping("/status")
    public String status() {
        return "Working on port: " + env.getProperty("local.server.port");
    }
}
