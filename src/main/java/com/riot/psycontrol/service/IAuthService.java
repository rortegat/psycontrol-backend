package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.UserDTO;
import com.riot.psycontrol.util.AuthRequest;
import com.riot.psycontrol.util.AuthResponse;
import com.riot.psycontrol.util.SignUp;

public interface IAuthService {
    AuthResponse signIn(AuthRequest authRequest);
    UserDTO signUp(SignUp signUp);
    AuthResponse refreshToken(String username);
}
