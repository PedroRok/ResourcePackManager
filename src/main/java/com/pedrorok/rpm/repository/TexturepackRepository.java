package com.pedrorok.rpm.repository;

import com.pedrorok.rpm.entity.Texturepack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@Repository
public interface TexturepackRepository extends JpaRepository<Texturepack, Long> {
    Optional<Texturepack> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<Texturepack> findByIsActiveTrue();
    List<Texturepack> findByNameContainingIgnoreCase(String name);
    List<Texturepack> findByAuthorName(String authorName);
}
