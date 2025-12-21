package com.batman.authservice.service;

import com.batman.authservice.dto.*;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface AuthService {
    void userRegister(RegistrationDetails registrationDetails);

    void userModify(Long id, RegistrationDetails registrationDetails);

    void deleteUser(Long id);

    List<UserDetails> getUserDetails();

    UserDetails getUserDetailsById(Long id);

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse refreshToken(RefreshRequest request);
}
