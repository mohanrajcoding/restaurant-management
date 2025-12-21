package com.batman.authservice.controller;

import com.batman.authservice.dto.UserDetails;
import com.batman.authservice.dto.RegistrationDetails;
import com.batman.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
private final AuthService authService;
@PostMapping("/register")
public ResponseEntity<?> userRegister(@RequestBody RegistrationDetails registrationDetails){
    authService.userRegister(registrationDetails);
    return ResponseEntity.ok(Collections.singletonMap("message","User registered Successfully"));
}
@PutMapping("/users/{id}")
public ResponseEntity<?> userModify(@PathVariable Long id,@RequestBody RegistrationDetails registrationDetails){
    System.out.println("Name:" + registrationDetails.getName());
    authService.userModify(id, registrationDetails);
    return ResponseEntity.ok(Collections.singletonMap("message","User modify Successfully"));
}
@DeleteMapping("/users/{id}")
public ResponseEntity<?> deleteUser(@PathVariable Long id){
    authService.deleteUser(id);
    return ResponseEntity.ok(Collections.singletonMap("message","User Deleted Successfully"));
}

@GetMapping("/users")
public ResponseEntity<List<UserDetails>> getUserDetails(){
    List<UserDetails> userDetails = authService.getUserDetails();
    return ResponseEntity.ok(userDetails);
}

@GetMapping("/users/{id}")
public ResponseEntity<UserDetails> getUserDetailsbyId(@PathVariable Long id){
        UserDetails userDetails = authService.getUserDetailsById(id);
        return ResponseEntity.ok(userDetails);
}
}

