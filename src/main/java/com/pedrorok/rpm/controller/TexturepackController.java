package com.pedrorok.rpm.controller;

import com.pedrorok.rpm.dto.TexturepackDTO;
import com.pedrorok.rpm.services.TexturepackService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@RestController
@RequestMapping("/api/texturepacks")
@RequiredArgsConstructor
public class TexturepackController {

    private final TexturepackService texturepackService;

    @GetMapping
    public ResponseEntity<List<TexturepackDTO>> getAllTexturepacks() {
        return ResponseEntity.ok(texturepackService.getAllTextures());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<TexturepackDTO> getTexturepackBySlug(String slug) {
        TexturepackDTO texturepackDTO = texturepackService.getTexturepackBySlug(slug);
        return ResponseEntity.ok(texturepackDTO);
    }
}
