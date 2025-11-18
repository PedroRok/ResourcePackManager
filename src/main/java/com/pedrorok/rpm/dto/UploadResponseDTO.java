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
public class UploadResponseDTO {
    private boolean success;
    private String message;
    private Long texturepackId;
    private Long versionId;
    private String version;
    private String downloadUrl;
}