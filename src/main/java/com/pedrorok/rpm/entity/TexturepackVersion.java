package com.pedrorok.rpm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@Entity
@Table(name = "texturepack_versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TexturepackVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "texturepack_id", nullable = false)
    @ToString.Exclude
    private Texturepack texturepack;

    @Column(nullable = false)
    private String version;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "changelog", length = 5000)
    private String changelog;

    @Column(name = "is_latest")
    @Builder.Default
    private Boolean isLatest = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
