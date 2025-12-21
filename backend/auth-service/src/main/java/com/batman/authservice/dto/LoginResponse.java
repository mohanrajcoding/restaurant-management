package com.batman.authservice.dto;

import com.batman.authservice.entity.Role;
import com.batman.authservice.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String userName;
    private String accessToken;
    private String refreshToken ;
    private String roleName;
    private String email;
}
