package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.entity.User;
import com.riot.psycontrol.model.SignUp;
import com.riot.psycontrol.repo.RoleRepo;
import com.riot.psycontrol.repo.UserRepo;
import com.riot.psycontrol.model.AuthResponse;
import com.riot.psycontrol.security.CustomException;
import com.riot.psycontrol.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
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

    @Autowired
    AuthenticationManager authenticationManager;

    public List<UserDTO> getUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> new UserDTO(user))
                .collect(Collectors.toList());
    }

    public UserDTO signUpUser(@NotNull SignUp signUp) {
        if (!userRepo.existsByUsername(signUp.getUsername())) {
            var user = new User();
            user.setUsername(signUp.getUsername());
            user.setPassword(passwordEncoder.encode(signUp.getPassword()));
            user.setFirstname(signUp.getFirstname());
            user.setLastname(signUp.getLastname());
            user.setEmail(user.getEmail());
            user.setRoles(Arrays.asList(roleRepo.findByRolename("DEMO")));
            return new UserDTO(userRepo.save(user));
        } else
            throw new CustomException("Username is already in use", HttpStatus.NOT_ACCEPTABLE);
    }

    public AuthResponse signInUser(@NotNull String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var u = userRepo.findByUsername(username);
            var auth = new AuthResponse();
            auth.setToken(jwtProvider.createToken(username, u.getRoles()));
            return auth;
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    public UserDTO getUserByUsername(@NotNull String username) {
        var user = userRepo.findByUsername(username);
        if (user != null)
            return new UserDTO(user);
        else
            throw new CustomException("This user does not exist", HttpStatus.BAD_REQUEST);
    }

    public UserDTO saveUser(@NotNull User user) {
        if (!userRepo.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    public AuthResponse refresh(@NotNull String username) {
        var user = userRepo.findByUsername(username);
        if (user != null) {
            var authenticated = new AuthResponse();
            var token = jwtProvider.createToken(username, user.getRoles());
            authenticated.setToken(token);
            return authenticated;
        } else throw new CustomException("Not found", HttpStatus.BAD_REQUEST);
    }
}
