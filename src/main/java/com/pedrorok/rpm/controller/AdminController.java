package com.pedrorok.rpm.controller;

import com.pedrorok.rpm.dto.TexturepackDTO;
import com.pedrorok.rpm.dto.UploadResponseDTO;
import com.pedrorok.rpm.services.TexturepackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/11/2025
 * @project ResourcePackManager
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TexturepackService texturepackService;

    @PostMapping("/texturepacks")
    public ResponseEntity<TexturepackDTO> createTexturepack(@Valid @RequestBody TexturepackDTO texturepackDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(texturepackService.createTexturepack(texturepackDTO));
    }

    @PostMapping(value = "/texturepacks/{slug}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDTO> uploadVersion(
            @PathVariable String slug,
            @RequestParam("version") String version,
            @RequestParam(value = "changelog", required = false) String changelog,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(UploadResponseDTO.builder()
                            .success(false)
                            .message("File is empty")
                            .build());
        }

        UploadResponseDTO response = texturepackService.uploadVersion(slug, version, changelog, file);
        return ResponseEntity.ok(response);
    }

}
