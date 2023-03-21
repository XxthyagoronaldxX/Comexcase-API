package com.startup.comexcase_api.domain.dtos.dealer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CreateDealerDTO {
    @NotBlank(message = "Name is mandatory.")
    private String name;

    @NotBlank(message = "Nickname is mandatory.")
    private String nickname;

    @Email
    @NotBlank(message = "Email is mandatory.")
    private String email;

    private String imgBackgroundUrl;

    private String imgProfileUrl;

    @NotBlank(message = "PhoneNumber is mandatory.")
    @Size(max = 20, message = "PhoneNumber must be less than 20 of length.")
    @Pattern(regexp = "\\(\\+55\\) [0-9]{2} [0-9]{5}-[0-9]{4}")
    private String phoneNumber;

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "Password must be higher than 8.")
    private String password;
}
