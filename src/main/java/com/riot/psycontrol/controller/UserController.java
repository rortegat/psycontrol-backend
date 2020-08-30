package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDTO> getUsers() {
        return userServiceImpl.getUsers();
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO getUser(@PathVariable String username) {
        return userServiceImpl.getUserByUsername(username);
    }

    @GetMapping("/whoami")
    public UserDTO whoami(Principal principal) {
        return userServiceImpl.getUserByUsername(principal.getName());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userServiceImpl.saveUser(userDTO);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        return userServiceImpl.updateUser(userDTO);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void removeUser(@PathVariable String username) {
        userServiceImpl.deleteUser(username);
    }

}
