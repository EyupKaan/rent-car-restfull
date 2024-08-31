package com.eyupkaan.rentcar.service;

import com.eyupkaan.rentcar.controller.UserController;
import com.eyupkaan.rentcar.model.User;
import com.eyupkaan.rentcar.model.assembler.UserAssembler;
import com.eyupkaan.rentcar.repository.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserAssembler assembler;

    UserService(UserRepository userRepository, UserAssembler assembler){
        this.repository = userRepository;
        this.assembler = assembler;
    }

    public CollectionModel<EntityModel<User>> getUsers(){
        List<EntityModel<User>> users = repository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUser()).withSelfRel());
    }

    public EntityModel<User> getUserById(Long id){
        User user = repository.findById(id).orElseThrow();
        return assembler.toModel(user);
    }

    public ResponseEntity<?> updateUser(User user,Long id){
        User info = repository.findById(id)
                .map(updated -> {
                    updated.setUserName(user.getUserName());
                    updated.setPassword(user.getPassword());
                    updated.setName(user.getName());
                    updated.setSurName(user.getSurName());
                    updated.setEMail(user.getEMail());
                    return repository.save((updated));
                })
                .orElseGet(() -> {
                    return repository.save(user);
                });
        EntityModel<User> updatedUser = assembler.toModel(info);

        return ResponseEntity
                .created(updatedUser.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(updatedUser);
    }

    public ResponseEntity<?> createUser(User user){
        EntityModel<User> newUser = assembler.toModel(repository.save(user));

        return ResponseEntity
                .created(newUser.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(newUser);
    }

    public ResponseEntity<?> deleteUser(Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
