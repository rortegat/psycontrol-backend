package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.entity.User;
import com.riot.psycontrol.repo.RoleRepo;
import com.riot.psycontrol.repo.UserRepo;
import com.riot.psycontrol.util.CustomException;
import com.riot.psycontrol.util.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    public List<UserDTO> getUsers() {
        return userRepo.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public UserDTO getUserByUsername(@NotNull String username) {
        var user = userRepo.findByUsername(username);
        if (user != null)
            return new UserDTO(user);
        else
            throw new CustomException("This user does not exist", HttpStatus.BAD_REQUEST);
    }

    public UserDTO saveUser(@NotNull UserDTO userDTO) {
        if (!userRepo.existsByUsername(userDTO.getUsername())) {
            var user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            user.setEmail(userDTO.getEmail());
            user.setRoles(userDTO.getRoles()
                    .stream()
                    .map(role -> roleRepo.findByRolename(role))
                    .collect(Collectors.toList()));
            return new UserDTO(userRepo.save(user));
        } else
            throw new CustomException("Username is already in use", HttpStatus.NOT_MODIFIED);
    }

    public UserDTO updateUser(@NotNull UserDTO userDTO) {
        var saved = userRepo.findByUsername(userDTO.getUsername());
        if (saved != null) {
            saved.setUsername(userDTO.getUsername());
            saved.setFirstname(userDTO.getFirstname());
            saved.setLastname(userDTO.getLastname());
            saved.setEmail(userDTO.getEmail());
            saved.setRoles(userDTO.getRoles()
                    .stream()
                    .map(role -> roleRepo.findByRolename(role))
                    .collect(Collectors.toList())
            );
            return new UserDTO(userRepo.save(saved));
        } else
            throw new CustomException("This user doesn't exist", HttpStatus.NOT_MODIFIED);
    }

    public void deleteUser(@NotNull String username) {
        var user = userRepo.findByUsername(username);
        if (user != null)
            userRepo.delete(user);
        else
            throw new CustomException("User does not exist", HttpStatus.BAD_REQUEST);
    }

}
