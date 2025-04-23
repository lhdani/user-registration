package com.challenger.users.service;

import com.challenger.users.dto.UserRequest;
import com.challenger.users.dto.UserResponse;
import com.challenger.users.entity.Phone;
import com.challenger.users.entity.User;
import com.challenger.users.exception.EmailAlreadyExistsException;
import com.challenger.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Setter
public class UserService {

    @Value("${regex.email}")
    private String emailRegex;

    @Value("${regex.password}")
    private String passwordRegex;

    private final UserRepository userRepository;

    public ResponseEntity<?> registerUser(UserRequest request) {
        if (!request.email().matches(emailRegex)) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (!request.password().matches(passwordRegex)) {
            throw new IllegalArgumentException("Password inválida");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setPhones(request.phones().stream().map(p -> {
            Phone phone = new Phone();
            phone.setNumber(p.number());
            phone.setCitycode(p.citycode());
            phone.setCountrycode(p.countrycode());
            return phone;
        }).toList());

        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);
        user.setToken(UUID.randomUUID().toString());
        user.setActive(true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new UserResponse(user.getId(), user.getCreated(), user.getModified(), user.getLastLogin(), user.getToken(), user.isActive())
        );
    }
}
