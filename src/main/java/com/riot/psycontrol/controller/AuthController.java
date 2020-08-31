package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.util.AuthRequest;
import com.riot.psycontrol.util.AuthResponse;
import com.riot.psycontrol.util.SignUp;
import com.riot.psycontrol.util.CustomException;
import com.riot.psycontrol.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    @Qualifier(value = "authServiceImpl")
    IAuthService authService;

    @PostMapping("/signup")
    public UserDTO singUp(@RequestBody SignUp signUp) {
        return authService.signUp(signUp);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.signIn(request);
    }


    @PostMapping("/refresh")
    public AuthResponse refreshToken(Principal principal) {
        if(principal==null)
            throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        return authService.refreshToken(principal.getName());
    }
}
