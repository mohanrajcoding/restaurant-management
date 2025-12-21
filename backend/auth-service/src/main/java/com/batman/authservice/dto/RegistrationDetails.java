package com.batman.authservice.dto;

import com.batman.authservice.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDetails {
    private String name;
    private String userName;
    private String email;
    private String password;
    private Long role_id;
    private Status status;
}
