package com.us.Torres.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class Index {
    @GetMapping
    public ResponseEntity status(){
        return ResponseEntity.ok("The app is up and running");
    }
}
