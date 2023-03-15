package org.barbaris.chatapi.controllers;

import org.barbaris.chatapi.models.UserModel;
import org.barbaris.chatapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private final UserService service;

    @Autowired
    public MainController(UserService service) {
        this.service = service;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel user) {
        if(user != null) {
            if(service.regUser(user).equals("OK")) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/register")
    public ResponseEntity<?> test(@RequestBody UserModel user) {
        System.out.println(user.getLogin());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}










