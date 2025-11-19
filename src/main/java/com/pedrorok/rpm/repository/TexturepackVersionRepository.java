package com.pedrorok.rpm.repository;

import com.pedrorok.rpm.entity.TexturepackVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@Repository
public interface TexturepackVersionRepository extends JpaRepository<TexturepackVersion, Long> {
    List<TexturepackVersion> findByTexturepackIdOrderByCreatedAtDesc(Long textureId);

    Optional<TexturepackVersion> findByTexturepackIdAndIsLatestTrue(Long textureId);

    @Modifying
    @Query("UPDATE TexturepackVersion ver SET ver.isLatest = false WHERE ver.texturepack.id = :textureId")
    void unmarkAllAsLatest(Long textureId);

    boolean existsByTexturepackIdAndVersion(Long textureId, String version);
}
