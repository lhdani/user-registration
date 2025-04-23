package com.challenger.users.controller;

import com.challenger.users.dto.UserRequest;
import com.challenger.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @Operation(description = "Realiza la creación de un nuevo usuario")
    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        return userService.registerUser(request);
    }

    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

}
