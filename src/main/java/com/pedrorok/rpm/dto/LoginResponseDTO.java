package com.pedrorok.rpm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/11/2025
 * @project ResourcePackManager
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private String type = "Bearer";
    private String username;
    private String role;
}