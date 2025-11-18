package com.pedrorok.rpm.services;

import com.pedrorok.rpm.dto.TexturepackDTO;
import com.pedrorok.rpm.dto.TexturepackVersionDTO;
import com.pedrorok.rpm.entity.Texturepack;
import com.pedrorok.rpm.entity.TexturepackVersion;
import com.pedrorok.rpm.repository.TexturepackRepository;
import com.pedrorok.rpm.repository.TexturepackVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
