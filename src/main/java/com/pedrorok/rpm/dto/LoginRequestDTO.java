package com.pedrorok.rpm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/11/2025
 * @project ResourcePackManager
 */
@Data
public class LoginRequestDTO {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username needs to be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain alphanumeric characters and underscores")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 99, message = "Password needs to be between 6 and 99 characters")
    private String password;
}
