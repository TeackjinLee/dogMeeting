package com.springboot.dogmeeting.api.user.dto;

import lombok.Getter;
import lombok.Setter;

public class UserRequest {

    @Getter
    @Setter
    public static class AddUserDto {
        private String email;
        private String password;
    }
}
