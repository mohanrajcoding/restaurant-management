package com.batman.authservice.service;

import com.batman.authservice.dto.*;
import com.batman.authservice.entity.Role;
import com.batman.authservice.entity.Status;
import com.batman.authservice.entity.Users;
import com.batman.authservice.exception.BusinessException;
import com.batman.authservice.repository.RoleRepository;
import com.batman.authservice.repository.UserRepository;
import com.batman.authservice.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
   // private BSUtil bsUtil;

    @Override
    public void userRegister(RegistrationDetails registrationDetails) {
        if (userRepository.existsByUserName(registrationDetails.getUserName())) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmail(registrationDetails.getEmail())) {
            throw new BusinessException("Email already exists");
        }


        Users user = new Users();
        Role role = roleRepository.findById(registrationDetails.getRole_id()).orElseThrow(
                ()->new BusinessException("Please select valid role"));
        user.setRole(role);
        user.setName(registrationDetails.getName());
        user.setUserName(registrationDetails.getUserName());
        user.setEmail(registrationDetails.getEmail());
        user.setStatus(Status.ACTIVE);
        user.setPassword(passwordEncoder.encode(registrationDetails.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void userModify(Long id, RegistrationDetails registrationDetails) {
        Users user = userRepository.findById(id).orElseThrow(
                ()->new BusinessException("Record not available for update") );

        if (registrationDetails.getName()!=null && registrationDetails.getName().isEmpty()){
            user.setName(registrationDetails.getName());
        }
        if (registrationDetails.getUserName()!=null && registrationDetails.getUserName().isEmpty()){
            user.setName(registrationDetails.getUserName());
        }
        if(registrationDetails.getEmail()!=null && registrationDetails.getEmail().isEmpty()){
            user.setEmail(registrationDetails.getEmail());
        }
        if (registrationDetails.getPassword()!=null && registrationDetails.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(registrationDetails.getPassword()));
        }
        if (registrationDetails.getRole_id()!=null && registrationDetails.getRole_id()!=0){
            user.setRole(roleRepository.findById(id).orElseThrow(
                    ()->new BusinessException("Please select valid role")));
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        Users user = userRepository.findById(id).orElseThrow(
                ()-> new BusinessException("No record found to delete for user id: " + id));
        userRepository.delete(user);
    }

    @Override
    public List<UserDetails> getUserDetails() {
        return userRepository.findAll().stream()
                .map(u->{
            UserDetails dto = new UserDetails();
            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setRoleName(u.getRole().getRoleName());
            dto.setEmail(u.getEmail());
            dto.setActiveStatus(u.getStatus());
            dto.setCreatedAt(u.getCreatedAt());
            dto.setUpdatedAt(u.getUpdatedAt());
            dto.setUserName(u.getUserName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDetails getUserDetailsById(Long id) {
        Users users = userRepository.findById(id).orElseThrow(()->new BusinessException("Please select valid user id"));
        UserDetails dto = new UserDetails();
        dto.setActiveStatus(users.getStatus());
        dto.setCreatedAt(users.getCreatedAt());
        dto.setId(users.getId());
        dto.setRoleName(users.getRole().getRoleName());
        dto.setName(users.getName());
        dto.setEmail(users.getEmail());
        dto.setUpdatedAt(users.getUpdatedAt());
        dto.setUserName(users.getUserName());
        return dto;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Users user = userRepository.findByUserName(loginRequest.getUserName())
                .orElseThrow(() -> new BusinessException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid username or password");
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        LoginResponse response = new LoginResponse();
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());
        response.setRoleName(user.getRole().getRoleName().name());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;

    }

    @Override
    public LoginResponse refreshToken(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException("Invalid refresh token");
        }

        Claims claims = jwtUtil.extractClaims(refreshToken);
        Users user = userRepository.findByEmail(claims.getSubject())
                .orElseThrow(() -> new BusinessException("User not found"));

        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        LoginResponse response = new LoginResponse();
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());
        response.setRoleName(user.getRole().getRoleName().name());
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);

        return response;
    }
}
