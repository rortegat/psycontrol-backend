package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    IUserService userService;

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

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
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
