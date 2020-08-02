package com.riot.psycontrol.controller;

import com.riot.psycontrol.entity.User;
import com.riot.psycontrol.model.AuthenticationRequest;
import com.riot.psycontrol.model.AuthenticationResponse;
import com.riot.psycontrol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired UserService userService;

    @GetMapping("/all") @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{username}") @PreAuthorize("hasAuthority('ADMIN')")
    public User getUser(@PathVariable String username){
    return userService.getUserByUsername(username);
    }

    @GetMapping("/whoami")
    public User whoami(){
        return userService.whoAmI();
    }

    @PostMapping("/signup")
    public User singUp(@RequestBody User user){
        return userService.signUpUser(user);
    }

    @PostMapping("/login")
        public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return userService.signInUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/add") @PreAuthorize("hasAuthority('ADMIN')")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/update") @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete/{username}") @PreAuthorize("hasAuthority('ADMIN')")
    public void removeUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

}
