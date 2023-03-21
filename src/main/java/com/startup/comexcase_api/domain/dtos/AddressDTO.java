package com.startup.comexcase_api.domain.dtos;

import com.startup.comexcase_api.domain.dtos.validators.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AddressDTO {
    @NotBlank
    @Pattern(regexp = "[0-9]{5}-[0-9]{3}", message = "ZipCode don't match with the pattern: 11100-111.")
    private String zipCode;

    @NotBlank
    private String state;

    @NotBlank
    private String country;

    @NotBlank
    private String neighborhood;

    @NotBlank
    private String city;

    @NotBlank
    private String houseNumber;
}
