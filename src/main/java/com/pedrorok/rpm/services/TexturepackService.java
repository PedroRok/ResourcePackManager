package com.pedrorok.rpm.services;

import com.pedrorok.rpm.dto.TexturepackDTO;
import com.pedrorok.rpm.dto.TexturepackVersionDTO;
import com.pedrorok.rpm.dto.UploadResponseDTO;
import com.pedrorok.rpm.entity.Texturepack;
import com.pedrorok.rpm.entity.TexturepackVersion;
import com.pedrorok.rpm.exception.StorageServiceException;
import com.pedrorok.rpm.repository.TexturepackRepository;
import com.pedrorok.rpm.repository.TexturepackVersionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@Service
@RequiredArgsConstructor
public class TexturepackService {

    private final TexturepackRepository texturepackRepository;
    private final TexturepackVersionRepository versionRepository;
    private final FileStorageService fileStorageService;
    private final VersionService versionService;

    public List<TexturepackDTO> getAllTextures() {
        return texturepackRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TexturepackDTO getTexturepackBySlug(String slug) {
        Texturepack texturepack = texturepackRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Texturepack not found: " + slug));
        return convertToFullDTO(texturepack);
    }

    private TexturepackDTO convertToDTO(Texturepack texturepack) {
        TexturepackVersionDTO latestVersion = versionRepository
                .findByTextureIdAndIsLatestTrue(texturepack.getId())
                .map(this::convertVersionToDTO)
                .orElse(null);

        return TexturepackDTO.builder()
                .id(texturepack.getId())
                .name(texturepack.getName())
                .slug(texturepack.getSlug())
                .authorName(texturepack.getAuthorName())
                .downloadCount(texturepack.getDownloadCount())
                .isActive(texturepack.getIsActive())
                .createdAt(texturepack.getCreatedAt())
                .updatedAt(texturepack.getUpdatedAt())
                .latestVersion(latestVersion)
                .build();
    }

    private TexturepackDTO convertToFullDTO(Texturepack texturepack) {
        TexturepackDTO dto = convertToDTO(texturepack);
        List<TexturepackVersionDTO> versions = versionRepository
                .findByTextureIdOrderByCreatedAtDesc(texturepack.getId())
                .stream()
                .map(this::convertVersionToDTO)
                .collect(Collectors.toList());
        dto.setVersions(versions);
        return dto;
    }

    private TexturepackVersionDTO convertVersionToDTO(TexturepackVersion version) {
        return TexturepackVersionDTO.builder()
                .id(version.getId())
                .version(version.getVersion())
                .fileName(version.getFileName())
                .fileSize(version.getFileSize())
                .changelog(version.getChangelog())
                .isLatest(version.getIsLatest())
                .createdAt(version.getCreatedAt())
                .downloadUrl("/api/download/" + version.getTexturepack().getSlug())
                .build();
    }

    @Transactional
    public TexturepackDTO createTexturepack(TexturepackDTO dto) {
        if (texturepackRepository.existsBySlug(dto.getSlug())) {
            throw new StorageServiceException("Texturepack with slug already exists: " + dto.getSlug());
        }

        Texturepack texturepack = Texturepack.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .authorName(dto.getAuthorName())
                .downloadCount(0L)
                .isActive(true)
                .build();

        Texturepack saved = texturepackRepository.save(texturepack);
        return convertToDTO(saved);
    }

    @Transactional
    public UploadResponseDTO uploadVersion(String slug, String version, String changelog, MultipartFile file) {
        Texturepack texturepack = texturepackRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Texturepack not found: " + slug));

        if (!versionService.isValidVersion(version)) {
            throw new StorageServiceException("Invalid version: " + version + ". Must follow semantic versioning (e.g., 1.0.0)");
        }

        if (versionRepository.existsByTextureIdAndVersion(texturepack.getId(), version)) {
            throw new StorageServiceException("This version already exists for texturepack: " + version);
        }

        String filePath = fileStorageService.storeFile(file, slug, version);
        long fileSize = fileStorageService.getFileSize(file);

        versionRepository.unmarkAllAsLatest(texturepack.getId());

        TexturepackVersion newVersion = TexturepackVersion.builder()
                .texturepack(texturepack)
                .version(version)
                .fileName(file.getOriginalFilename())
                .filePath(filePath)
                .fileSize(fileSize)
                .changelog(changelog)
                .isLatest(true)
                .build();

        TexturepackVersion saved = versionRepository.save(newVersion);

        return UploadResponseDTO.builder()
                .success(true)
                .message("Upload successful")
                .texturepackId(texturepack.getId())
                .versionId(saved.getId())
                .version(version)
                .downloadUrl("/api/download/" + slug)
                .build();
    }
}
