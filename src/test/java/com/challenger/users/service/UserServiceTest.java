package com.challenger.users.service;

import com.challenger.users.dto.PhoneRequest;
import com.challenger.users.dto.UserRequest;
import com.challenger.users.dto.UserResponse;
import com.challenger.users.entity.User;
import com.challenger.users.exception.EmailAlreadyExistsException;
import com.challenger.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final String validEmail = "john.doe@example.com";
    private final String validPassword = "Password1";

    @BeforeEach
    public void setUp() {
        userService.setEmailRegex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        userService.setPasswordRegex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    }

    @Test
    public void testRegisterUser_Success() {
        UserRequest request = new UserRequest("John", validEmail, validPassword,
                List.of(new PhoneRequest("123456", "1", "57")));

        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userService.registerUser(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isInstanceOf(UserResponse.class);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmailInvalid() {
        UserRequest request = new UserRequest("John", "invalid-email", validPassword,
                List.of(new PhoneRequest("123456", "1", "57")));

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email inválido");
    }

    @Test
    public void testRegisterUser_PasswordInvalid() {
        UserRequest request = new UserRequest("John", validEmail, "password",
                List.of(new PhoneRequest("123456", "1", "57")));

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password inválida");
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        UserRequest request = new UserRequest("John", validEmail, validPassword,
                List.of(new PhoneRequest("123456", "1", "57")));

        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }
}