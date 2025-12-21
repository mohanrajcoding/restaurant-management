package com.batman.authservice.dto;

import com.batman.authservice.entity.RoleName;
import com.batman.authservice.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
    private Long id;
    private String name;
    private String userName;
    private String email;
    private Status activeStatus;
    private Instant createdAt;
    private Instant updatedAt;
    private RoleName roleName;

}
