package org.barbaris.chatapi.controllers;

import org.barbaris.chatapi.models.UserModel;
import org.barbaris.chatapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserModel user) {
        if(user != null) {
            String response = service.logUser(user);

            if(response.equals("OK")) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else if (response.equals("Not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserModel user) {
        if(user != null) {
            String response = service.regUser(user);

            if(response.equals("OK")) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else if(response.equals("Login is already taken")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}










