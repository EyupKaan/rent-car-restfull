package com.eyupkaan.rentcar.controller;

import com.eyupkaan.rentcar.model.User;
import com.eyupkaan.rentcar.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> getAllUser(){
        return userService.getUsers();
    }

    @GetMapping("/user/{id}")
    public EntityModel<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
    @PutMapping("/user/{id}")

    public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable Long id){
        return userService.updateUser(user, id);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<?> createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}
