package com.startup.comexcase_api.api.config.security.userdetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CustomUserDetailsDTO {
    private String username;
    private String password;
}
