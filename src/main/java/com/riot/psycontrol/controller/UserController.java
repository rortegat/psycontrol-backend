package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.entity.User;
import com.riot.psycontrol.model.AuthRequest;
import com.riot.psycontrol.model.AuthResponse;
import com.riot.psycontrol.model.SignUp;
import com.riot.psycontrol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/whoami")
    public UserDTO whoami(Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }

    @PostMapping("/signup")
    public UserDTO singUp(@RequestBody SignUp signUp) {
        return userService.signUpUser(signUp);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return userService.signInUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(Principal principal) {
        return userService.refresh(principal.getName());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void removeUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

}
