package com.example.restapi.controller;

import com.example.restapi.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceTestController {

    private final UserService userService;

    public ServiceTestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test/service")
    public String testService() {
        return "Total Users: " + userService.getAllUsers().size();
    }
}