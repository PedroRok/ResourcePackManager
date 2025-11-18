package com.pedrorok.rpm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/11/2025
 * @project ResourcePackManager
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TexturepackVersionDTO {
    private Long id;
    private String version;
    private String fileName;
    private Long fileSize;
    private String changelog;
    private Boolean isLatest;
    private LocalDateTime createdAt;
    private String downloadUrl;
}