package com.challenger.users.dto;

import java.util.List;

public record UserRequest(String name, String email, String password, List<PhoneRequest> phones) {

}
