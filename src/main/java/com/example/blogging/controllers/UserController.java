package com.example.blogging.controllers;

import com.example.blogging.entity.User;
import com.example.blogging.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@ApiResponses({@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
        @ApiResponse(responseCode = "201", description = "Created")
})
public class UserController {
    @Autowired
    UserService userService;
//    @Operation(
//            description = "Creating a User",
//            summary = "Creating of users"
//
//    )
////    Creating of users
//    @PostMapping("/api/v1/user")
//    public User saveUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }
    @Operation(
            description = "Getting a User",
            summary = "Getting of user  by Id"

    )
//    Getting of user by id
    @GetMapping("/api/v1/user/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    @Operation(
            description = "Updating of a user",
            summary = "Updating of a user by Id"

    )
//    Updating of user by id
    @PutMapping("/api/v1/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        // Ensure the id is set in the updateUser object
        updateUser.setId(id);

        // Call updateUser method and handle the response
        try {
            User updatedUser = userService.updateUser(updateUser);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            description = "Deleting a User",
            summary = "Deleting of user by Id"

    )
//    Deleting of user by id
    @DeleteMapping("/api/v1/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
