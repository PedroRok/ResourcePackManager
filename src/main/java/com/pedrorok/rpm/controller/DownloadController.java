package com.pedrorok.rpm.controller;

import com.pedrorok.rpm.entity.TexturepackVersion;
import com.pedrorok.rpm.repository.TexturepackRepository;
import com.pedrorok.rpm.repository.TexturepackVersionRepository;
import com.pedrorok.rpm.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.NoSuchElementException;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@RestController
@RequestMapping("/api/download")
@RequiredArgsConstructor
public class DownloadController {

    private final TexturepackRepository texturepackRepository;
    private final TexturepackVersionRepository versionRepository;
    private final FileStorageService fileStorageService;

    @GetMapping("/slug")
    public ResponseEntity<Resource> downloadLatest(@PathVariable String slug) {
        var texturepack = texturepackRepository.findBySlug(slug).orElseThrow(() -> new NoSuchElementException("Resource not found: " + slug));

        TexturepackVersion latestVersion = versionRepository.findByTextureIdAndIsLatestTrue(texturepack.getId()).orElseThrow(() -> new NoSuchElementException("Resource not found: " + slug));

        // todo: increment download count

        return buildDownloadResponse(latestVersion);
    }

    @GetMapping("/{slug}/version/{version}")
    public ResponseEntity<Resource> downloadSpecificVersion(
            @PathVariable String slug,
            @PathVariable String version) {
        var texturepack = texturepackRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + slug));

        TexturepackVersion requestedVersion = versionRepository
                .findByTextureIdOrderByCreatedAtDesc(texturepack.getId())
                .stream()
                .filter(ver -> ver.getVersion().equals(version))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Version not found: " + version));

        // todo: increment download count
        return buildDownloadResponse(requestedVersion);
    }

    private ResponseEntity<Resource> buildDownloadResponse(TexturepackVersion version) {
        try {
            Path filePath = fileStorageService.loadFile(version.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new NoSuchElementException("File not found");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + version.getFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            throw new NoSuchElementException("Error during file loading: " + e.getMessage());
        }
    }
}
