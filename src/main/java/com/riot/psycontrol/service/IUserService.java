package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.UserDTO;

import java.util.List;

public interface IUserService {
    List<UserDTO> getUsers();
    UserDTO getUserByUsername(String username);
    UserDTO saveUser(UserDTO userDTO);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(String username);
}
