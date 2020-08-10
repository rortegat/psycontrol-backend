package com.riot.psycontrol.service.impl;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.entity.User;
import com.riot.psycontrol.model.AuthRequest;
import com.riot.psycontrol.model.AuthResponse;
import com.riot.psycontrol.model.SignUp;
import com.riot.psycontrol.repo.RoleRepo;
import com.riot.psycontrol.repo.UserRepo;
import com.riot.psycontrol.security.CustomException;
import com.riot.psycontrol.security.jwt.JwtProvider;
import com.riot.psycontrol.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service(value = "AuthServiceImpl")
public class AuthServiceImpl implements IAuthService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse signIn(AuthRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            var auth = new AuthResponse();
            auth.setToken(jwtProvider.createToken(req.getUsername()));
            return auth;
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public UserDTO signUp(SignUp signUp) {
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

    @Override
    public AuthResponse refreshToken(String username) {
        var user = userRepo.findByUsername(username);
        if (user != null) {
            var authenticated = new AuthResponse();
            var token = jwtProvider.createToken(username);
            authenticated.setToken(token);
            return authenticated;
        } else
            throw new CustomException("Not found", HttpStatus.BAD_REQUEST);
    }
}
