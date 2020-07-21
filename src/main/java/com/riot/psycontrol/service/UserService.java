package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.User;
import com.riot.psycontrol.repo.UserRepo;
import com.riot.psycontrol.model.AuthenticationResponse;
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

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    RoleService roleService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User signUpUser(User user) {
        if (!userRepo.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUsername(user.getUsername());
            user.setEmail(user.getEmail());
            user.setRoles(Arrays.asList(roleService.getRole("DEMO")));
            userRepo.save(user);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.NOT_ACCEPTABLE);
        }
        return user;
    }

    public AuthenticationResponse signInUser(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User u = userRepo.findByUsername(username);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setUsername(username);
            authenticationResponse.setFirstname(u.getFirstname());
            authenticationResponse.setLastname(u.getLastname());
            authenticationResponse.setEmail(u.getEmail());
            authenticationResponse.setRoles(u.getRoles());
            authenticationResponse.setToken(jwtProvider.createToken(username, u.getRoles()));
            return authenticationResponse;
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }


    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User saveUser(User user) {
        if (!userRepo.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.NOT_MODIFIED);
        }
    }

    public User updateUser(User user) {
        User saved = userRepo.findByUsername(user.getUsername());
        if (saved != null) {
            saved.setFirstname(user.getFirstname());
            saved.setLastname(user.getLastname());
            saved.setEmail(user.getEmail());
            saved.setRoles(user.getRoles());
            return userRepo.save(saved);
        } else {
            throw new CustomException("This user doesn't exist", HttpStatus.NOT_MODIFIED);
        }
    }

    public void deleteUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user != null)
            userRepo.delete(user);
        else
            throw new CustomException("User does not exist", HttpStatus.BAD_REQUEST);
    }

    public User whoAmI() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return getUserByUsername(username);
    }

    public String refresh(String username) {
        return jwtProvider.createToken(username, userRepo.findByUsername(username).getRoles());
    }
}
