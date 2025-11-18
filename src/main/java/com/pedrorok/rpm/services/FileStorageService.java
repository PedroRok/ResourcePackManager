package com.pedrorok.rpm.services;

import com.pedrorok.rpm.exception.StorageServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@Service
public class FileStorageService {

    private final Path pathToStorage;

    public FileStorageService(@Value("${app.storage.path}") String pathToStorage) {
        this.pathToStorage = Paths.get(pathToStorage).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.pathToStorage);
        } catch (Exception e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    public String storeFile(MultipartFile file, String textureSlug, String version) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new StorageServiceException("Invalid file name");
        }

        if (!originalFileName.endsWith(".zip")) {
            throw new StorageServiceException("Only ZIP files are allowed");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
            (!contentType.equals("application/zip") &&
             !contentType.equals("application/x-zip-compressed"))) {
            throw new StorageServiceException("Invalid file type. Only ZIP is allowed");
        }

        if (textureSlug == null || textureSlug.isEmpty() ||
            textureSlug.contains("..") || textureSlug.contains("/") || textureSlug.contains("\\")) {
            throw new StorageServiceException("Invalid texture pack slug");
        }

        String sanitizedSlug = textureSlug.replaceAll("[^a-zA-Z0-9-_]", "_");
        String newFileName = sanitizedSlug + "_v" + version + ".zip";

        try {
            if (newFileName.contains("..")) {
                throw new StorageServiceException("Invalid file path");
            }

            Path texturepackDir = this.pathToStorage.resolve(sanitizedSlug).normalize();

            if (!texturepackDir.startsWith(this.pathToStorage)) {
                throw new StorageServiceException("Invalid texture pack directory");
            }

            Files.createDirectories(texturepackDir);

            Path targetLocation = texturepackDir.resolve(newFileName).normalize();

            if (!targetLocation.startsWith(this.pathToStorage)) {
                throw new StorageServiceException("Invalid target file path");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation);
            }

            return textureSlug + "/" + newFileName;
        } catch (IOException e) {
            throw new StorageServiceException("Could not store file " + newFileName, e);
        }
    }

    public Path loadFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new StorageServiceException("Invalid file path");
        }

        String sanitizedPath = filePath.replaceAll("[^a-zA-Z0-9-_/\\.]", "_");

        if (sanitizedPath.contains("..")) {
            throw new StorageServiceException("Invalid file path");
        }

        Path targetLocation = this.pathToStorage.resolve(sanitizedPath).normalize();

        if (!targetLocation.startsWith(this.pathToStorage)) {
            throw new StorageServiceException("Invalid file path");
        }

        if (!Files.exists(targetLocation)) {
            throw new StorageServiceException("File not found: " + sanitizedPath);
        }

        return targetLocation;
    }

    public void deleteFile(String filePath) {
        try {
            Path targetLocation = loadFile(filePath);
            Files.deleteIfExists(targetLocation);
        } catch (IOException e) {
            throw new StorageServiceException("Could not delete file: " + filePath, e);
        }
    }

    public long getFileSize(MultipartFile file) {
        return file.getSize();
    }
}
