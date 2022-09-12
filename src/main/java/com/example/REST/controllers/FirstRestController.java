package com.example.REST.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class FirstRestController {

    @GetMapping("/hello")
public String sayHello(){

    return "Hello";
}

}
