package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.model.AuthRequest;
import com.riot.psycontrol.model.AuthResponse;
import com.riot.psycontrol.model.SignUp;

public interface IAuthService {
    AuthResponse signIn(AuthRequest authRequest);
    UserDTO signUp(SignUp signUp);
    AuthResponse refreshToken(String username);
}
